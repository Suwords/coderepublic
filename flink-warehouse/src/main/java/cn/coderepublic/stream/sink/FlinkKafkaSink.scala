package cn.coderepublic.stream.sink

import akka.remote.serialization.StringSerializer
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaProducer, KafkaSerializationSchema}
import org.apache.kafka.clients.producer.ProducerRecord

import java.lang
import java.util.Properties
/**
 * @description: Kafka Sink
 * @author: shier
 * @date: 2022/12/28 21:13
 */
object FlinkKafkaSink {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)
    val result: DataStream[(String, Int)] = stream.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(0)
      .sum(1)

    val prop = new Properties()
    prop.setProperty("bootstrap.servers", "cdh001:9092")
    prop.setProperty("key.serializer", classOf[StringSerializer].getName)
    prop.setProperty("value.serializer", classOf[StringSerializer].getName)

    result.addSink(new FlinkKafkaProducer[(String, Int)]("wc", new KafkaSerializationSchema[(String, Int)] {
      override def serialize(t: (String, Int), aLong: lang.Long) = {
        new ProducerRecord("wc", t._1.getBytes, (t._2+"").getBytes())
      }
    }, prop
    , FlinkKafkaProducer.Semantic.EXACTLY_ONCE
    ))

    env.execute()
  }
}
