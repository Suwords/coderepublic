package cn.coderepublic.data

import akka.remote.serialization.StringSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties
import scala.io.Source

/**
 * @description: 交通数据kafka生产者
 * @author: shier
 * @date: 2022/12/28 09:20
 */
object TrafficKafkaProducer {
  def main(args: Array[String]): Unit = {
    val prop = new Properties()
    prop.setProperty("bootstrap.servers", "cdh001:9092")
    prop.setProperty("key.serializer", classOf[StringSerializer].getName)
    prop.setProperty("value.serializer", classOf[StringSerializer].getName)

    val producer = new KafkaProducer[String, String](prop)

    val iterator: Iterator[String] = Source.fromFile("flink-warehouse/src/data/carFlow_all_column_test.txt", "UTF-8").getLines()

    for (i <- 1 to 100) {
      for (line <- iterator) {
        // 将需要的字段值生产到 kafka 集群 car_id monitor_id event-time speed
        // 车牌号 卡口号 车辆通过时间 通过速度
        val splits: Array[String] = line.split(",")
        val monitorID: String = splits(0).replace("'", "")
        val car_id: String = splits(2).replace("'", "")
        val eventTime: String = splits(4).replace("'", "")
        val speed: String = splits(6).replace("'", "")

        if (!"00000000".equals(car_id)) {
          val event = new StringBuilder
          event.append(monitorID + "\t").append(car_id + "\t").append(eventTime + "\t").append(speed)
          producer.send(new ProducerRecord[String, String]("flink-kafka", event.toString()))
        }
        Thread.sleep(500)
      }
    }
  }
}
