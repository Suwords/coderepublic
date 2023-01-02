package cn.coderepublic.stream.source

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, KafkaDeserializationSchema}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.Properties

/**
 * @description: 从 Kafka 中读取数据
 * @author: shier
 * @date: 2022/12/27 10:53
 */
object FlinkKafkaSource {
  def main(args: Array[String]): Unit = {
    // 使用本地模式演示，并开启WEb UI
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    // 从kafka中读取数据
    // 方式一：
//    val kafkaStream: DataStream[String] = env.fromSource(kafkaSource1, WatermarkStrategy.noWatermarks(), "kafka source")

    // 方式二：
    val prop = new Properties()
    prop.setProperty("bootstrap.servers", "cdh001:9092")
    prop.setProperty("group.id", "flink-kafka-1")
    prop.setProperty("key.deserializer", classOf[StringDeserializer].getName)
    prop.setProperty("value.deserializer", classOf[StringDeserializer].getName)
    /**
     * earliest：从头开始消费，旧数据会频繁消费
     * latest：从最近的数据开始消费，不再消费旧数据
     */
    prop.setProperty("auto.offset.reset", "latest")
    val kafkaStream: DataStream[(String, String)] = env.addSource(new FlinkKafkaConsumer[(String, String)]("flink-kafka", new KafkaDeserializationSchema[(String, String)] {
      override def isEndOfStream(t: (String, String)) = false

      override def deserialize(consumerRecord: ConsumerRecord[Array[Byte], Array[Byte]]) = {
        val key = new String(consumerRecord.key(), "UTF-8")
        val value = new String(consumerRecord.value(), "UTF-8")
        (key, value)
      }

      // 指定返回数据的类型
      override def getProducedType = createTuple2TypeInformation(createTypeInformation[String],
        createTypeInformation[String])
    }, prop))

    kafkaStream.print()

    env.execute()
  }

  def kafkaSource1: KafkaSource[String] = {
    val kafkaSource: KafkaSource[String] = KafkaSource.builder()
      .setBootstrapServers("cdh001:9092") // 必填
      .setTopics("flink-kafka") // 必填
      .setGroupId("flink-kafka-0") // 必填
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(new SimpleStringSchema())
      .build()
    kafkaSource
  }

}
