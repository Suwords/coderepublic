package cn.coderepublic.stream.sink

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.configuration.Configuration
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

import java.io.PrintStream
import java.net.{InetAddress, Socket}


/**
 * @description: Flink socket Sink
 * @author: shier
 * @date: 2022/12/28 21:23
 */
object FlinkSocketSink {
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
      .addSink(new SocketCustomSink("localhost", 8888))

    env.execute()
  }

  // 幂等性写入socket
  class SocketCustomSink(host: String, port:Int) extends RichSinkFunction[(String, Int)] {
    var socket: Socket = _
    var writer: PrintStream = _
    // 每来一个元素都会调用一次
    override def invoke(value: (String, Int), context: SinkFunction.Context): Unit = {
      writer.println(value._1 + "\t" + value._2)
      writer.flush()
    }

    // thread 初始化时执行一次
    override def open(parameters: Configuration): Unit = {
      socket = new Socket(InetAddress.getByName(host), port)
      writer = new PrintStream(socket.getOutputStream)
    }

    // thread 关闭的时候执行一次
    override def close(): Unit = {
      writer.close()
      socket.close()
    }
  }
}
