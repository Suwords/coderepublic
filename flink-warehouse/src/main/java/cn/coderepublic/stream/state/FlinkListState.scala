package cn.coderepublic.stream.state

import cn.coderepublic.stream.state.FlinkValueState.CarInfo
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.{AggregateFunction, RichMapFunction}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{AggregatingState, AggregatingStateDescriptor, ListState, ListStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala._

import java.text.SimpleDateFormat
import scala.collection.JavaConverters.iterableAsScalaIterableConverter

/**
 * @description: 统计每辆车的运行轨迹，即这辆车的信息按时间顺序、卡口号串联起来
 * @author: shier
 * @date: 2022/12/31 10:43
 */
object FlinkListState {
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

    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    stream.map(data => {
      val arr: Array[String] = data.split(" ")
      // 卡口 车辆 事件时间 车速
      val time = sdf.parse(arr(2)).getTime
      (arr(0), arr(1), time, arr(3).toLong)
    }).keyBy(_._2)
      .map(new RichMapFunction[(String, String, Long, Long), (String, String)] {
        // event-time monitor-id
        private var speedInfos: ListState[(Long, String)] = _


        override def open(parameters: Configuration) = {
          val listStateDesc = new ListStateDescriptor[(Long, String)]("speedInfos", createTypeInformation[(Long, String)])
          speedInfos = getRuntimeContext.getListState(listStateDesc)
        }

        override def map(in: (String, String, Long, Long)) = {
          speedInfos.add(in._3, in._1)
          val infos = speedInfos.get().asScala.seq
          val sortList = infos.toList.sortBy(x => x._1).reverse
          val builder = new StringBuilder
          for (elem <- sortList) {
            builder.append(elem._2 + "\t")
          }
          (in._2, builder.toString())
        }
      }).print()

    env.execute()
  }
}
