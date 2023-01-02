package cn.coderepublic.stream.sink

import akka.stream.actor.WatermarkRequestStrategy
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.configuration.Configuration
import org.apache.flink.connector.kafka.source.{KafkaSource, KafkaSourceBuilder}
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

import java.sql.{Connection, DriverManager, PreparedStatement}
/**
 * @description: Flink Mysql Sink
 * @author: shier
 * @date: 2022/12/28 21:23
 */
object FlinkMySqlSink {
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
      .addSink(new MySQLCustomSink)

    env.execute()
  }

  // 幂等性写入外部数据库MySQL
  class MySQLCustomSink extends RichSinkFunction[(String, Int)] {
    var conn: Connection = _
    var insertPst: PreparedStatement = _
    var updatePst: PreparedStatement = _

    // 每来一个元素都会调用一次
    override def invoke(value: (String, Int), context: SinkFunction.Context): Unit = {
      println(value)
      updatePst.setInt(1, value._2)
      updatePst.setString(2, value._1)
      updatePst.execute()
      println(updatePst.getUpdateCount)
      if (updatePst.getUpdateCount == 0) {
        println("insert")
        insertPst.setString(1, value._1)
        insertPst.setInt(2, value._2)
        insertPst.execute()
      }
    }

    // thread 初始化时执行一次
    override def open(parameters: Configuration): Unit = {
      conn = DriverManager.getConnection("jdbc://mysql://localhost:3306/test", "root", "123123")
      insertPst = conn.prepareStatement("insert into car_flow(monitorId,count) values(?,?)")
      updatePst = conn.prepareStatement("update car_flow set count = ? where monitorId = ?")
    }

    // thread 关闭的时候执行一次
    override def close(): Unit = {
      insertPst.close()
      updatePst.close()
      conn.close()
    }
  }
}
