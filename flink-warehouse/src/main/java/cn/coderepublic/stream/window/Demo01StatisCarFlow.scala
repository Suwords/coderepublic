package cn.coderepublic.stream.window

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.{AggregateFunction, ReduceFunction}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.{ProcessWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
/**
 * @description: 使用增量聚合函数统计最近20s内，各个卡口的车流量
 * @author: shier
 * @date: 2023/1/1 20:17
 */
object Demo01StatisCarFlow {
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
      val arr: Array[String] = data.split("\t")
      val monitorId: String = arr(0)
      (monitorId, 1)
    }).keyBy(_._1)
      .timeWindow(Time.seconds(10))
//      .reduce(new ReduceFunction[(String, Int)] {
//        override def reduce(t: (String, Int), t1: (String, Int)) = {
//          (t._1, t._2 + t1._2)
//        }
//      }).print()
      .aggregate(new AggregateFunction[(String, Int), Int, Int] {
        override def createAccumulator() = 0

        override def add(in: (String, Int), acc: Int) = acc + in._2

        override def getResult(acc: Int) = acc

        override def merge(acc: Int, acc1: Int) = acc + acc1
      },
//        new WindowFunction[Int, (String, Int), String, TimeWindow] {
//          override def apply(key: String, window: TimeWindow, input: Iterable[Int], out: Collector[(String, Int)]): Unit = {
//            for (elem <- input) {
//              out.collect((key, elem))
//            }
//          }
//        }
        new ProcessWindowFunction[Int, (String, Int), String, TimeWindow] {
          override def process(key: String, context: Context, elements: Iterable[Int], out: Collector[(String, Int)]): Unit = {
            for (elem <- elements) {
              out.collect((key, elem))
            }
          }
        }
      ).print()

    env.execute()
  }
}
