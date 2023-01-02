package cn.coderepublic.stream.sink

import cn.coderepublic.stream.util.DateUtils
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.configuration.Configuration
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

import java.util.{Date, Properties}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{HTable, Put}
import org.apache.hadoop.hbase.util.Bytes


/**
 * @description: Flink hbase Sink
 * @author: shier
 * @date: 2022/12/28 21:23
 */
object FlinkHBaseSink {
  case class CarInfo(monitorId: String, carId: String, eventTime: String, speed: Long)
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val kafkaSource = KafkaSource.builder()
      .setBootstrapServers("cdh001:9092")
      .setTopics("flink-kafka")
      .setGroupId("flink-kafka-001")
      .setValueOnlyDeserializer(new SimpleStringSchema())
      .setStartingOffsets(OffsetsInitializer.latest())
      .build()

    val stream: DataStream[String] = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "flink-kafka")

    stream.map(data => {
      val splits: Array[String] = data.split("\t")
      val monitorId = splits(0)
      (monitorId, 1)
    }).keyBy(_._1)
      .reduce(new ReduceFunction[(String, Int)] {
        override def reduce(t: (String, Int), t1: (String, Int)) = {
          // t 上次聚合完的结果，t1 当前的数据
          (t._1, t._2 + t1._2)
        }
      })
      .process(new ProcessFunction[(String, Int), (String, Int)] {
        var htab: HTable = _

        override def open(parameters: Configuration) = {
          val conf = HBaseConfiguration.create()
          conf.set("hbase.zookeeper.quorum", "cdh001:2181")
          val hbaseName = "car_flow"
          htab = new HTable(conf, hbaseName)
        }

        override def close() = {
          htab.close()
        }

        override def processElement(i: (String, Int), context: ProcessFunction[(String, Int), (String, Int)]#Context, collector: Collector[(String, Int)]) = {
          // rowkey:monitorid 时间戳（分钟） value: 车流量
          val min = DateUtils.getMin(new Date())
          val put = new Put(Bytes.toBytes(i._1))
          put.addColumn(Bytes.toBytes("count"), Bytes.toBytes(min), Bytes.toBytes(i._2))
          htab.put(put)
        }
      })

    env.execute()
  }
}
