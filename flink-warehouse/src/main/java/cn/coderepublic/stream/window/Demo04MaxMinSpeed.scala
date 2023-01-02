package cn.coderepublic.stream.window

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessAllWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
 * @description: 每隔10s统计出窗口内所有车辆的最大及最小速度
 * @author: shier
 * @date: 2023/1/2 12:29
 */
object Demo04MaxMinSpeed {
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
    }).timeWindowAll(Time.seconds(10))
    // 注意：想要全局排序并行度需要设置为1
      .process(new ProcessAllWindowFunction[(String, Int), String, TimeWindow] {
        override def process(context: Context, elements: Iterable[(String, Int)], out: Collector[String]): Unit = {
          val sortList: List[(String, Int)] = elements.toList.sortBy(_._2)
          println(sortList)
          val minSpeedInfo: (String, Int) = sortList.head
          val maxSpeedInfo: (String, Int) = sortList.last
          val startWindowTime: Long = context.window.getStart
          val endWindowTime: Long = context.window.getEnd
          out.collect("窗口起始时间：" + startWindowTime + "结束时间：" + endWindowTime + " 最小车辆 速度车牌号：" + minSpeedInfo._1 + "车速：" + minSpeedInfo._2
          + "\t最大车辆速度车牌号：" + maxSpeedInfo._1 + "车速：" + maxSpeedInfo._2)
        }
      }).print()

    env.execute()
  }
}
