package cn.coderepublic.stream.window

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
/**
 * @description: 每隔10s统计每辆汽车的平均速度
 * @author: shier
 * @date: 2023/1/2 12:29
 */
object Demo02SpeedAVG {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val kafkaSource: KafkaSource[String] = KafkaSource.builder()
      .setBootstrapServers("cdh001:9092") // 必填
      .setTopics("flink-kafka") // 必填
      .setGroupId("flink-kafka-0") // 必填
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(new SimpleStringSchema())
      .build()

    val stream: DataStream[String] = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "flink-kafka")

    stream.map(data => {
      val splits: Array[String] = data.split("\t")
      (splits(1), splits(3).toInt)
    }).keyBy(_._1)
      .timeWindow(Time.seconds(10))
      .aggregate(new AggregateFunction[(String, Int), (String, Int, Int), (String, Double)] {
        override def createAccumulator() = ("",0 , 0)

        override def add(in: (String, Int), acc: (String, Int, Int)) = {
          (in._1, in._2 + acc._2, acc._3 + 1)
        }

        override def getResult(acc: (String, Int, Int)) = {
          (acc._1, acc._2.toDouble / acc._3)
        }

        override def merge(acc: (String, Int, Int), acc1: (String, Int, Int)) = {
          (acc._1, acc._2 + acc1._2, acc._3 + acc1._3)
        }
      }).print()

    env.execute()
  }
}
