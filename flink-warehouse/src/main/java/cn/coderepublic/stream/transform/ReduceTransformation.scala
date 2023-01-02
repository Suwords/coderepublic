package cn.coderepublic.stream.transform

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema
import org.apache.flink.streaming.api.scala._

/**
 * @description: reduce 算子
 * 读取 Kafka 数据，实时统计各个卡口下的车流量
 * @author: shier
 * @date: 2022/12/28 09:08
 */
object ReduceTransformation {
  case class CarFlow(car_id: String,monitor_id: String,eventTime: String,speed: Double)
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val kafkaSource: KafkaSource[String] = KafkaSource.builder()
      .setBootstrapServers("cdh001:9092")
      .setTopics("flink-kafka")
      .setGroupId("flink-kafka-0933")
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(new SimpleStringSchema())
      .build()

    val stream: DataStream[String] = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "traffic-kafka")

    stream.map(data => {
      val splits: Array[String] = data.split("\t")
      val carFlow: CarFlow = CarFlow(splits(0), splits(1), splits(2), splits(3).toDouble)
      (carFlow, 1)
    }).keyBy(_._1.monitor_id)
      .sum(1)
      .print()

    env.execute()
  }
}
