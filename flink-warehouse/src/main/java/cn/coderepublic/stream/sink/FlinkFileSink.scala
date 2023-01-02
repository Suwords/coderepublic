package cn.coderepublic.stream.sink

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.serialization.{SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.configuration.Configuration
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

import java.sql.{Connection, DriverManager, PreparedStatement}

/**
 * @description: Flink file Sink
 * @author: shier
 * @date: 2022/12/28 21:23
 */
object FlinkFileSink {
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

    val restStream: DataStream[String] = stream.map(data => {
      val splits: Array[String] = data.split("\t")
      val monitorId = splits(0)
      (monitorId, 1)
    }).keyBy(_._1)
      .reduce(new ReduceFunction[(String, Int)] {
        override def reduce(t: (String, Int), t1: (String, Int)) = {
          // t 上次聚合完的结果，t1 当前的数据
          (t._1, t._2 + t1._2)
        }
      }).map(x => x._1 + "\t" + x._2)
    //设置文件滚动策略 
    val rolling:DefaultRollingPolicy[String,String] = DefaultRollingPolicy.create() 
    //当文件超过2s没有写入新数据，则滚动产生一个小文件 
      .withInactivityInterval(2000) 
    //文件打开时间超过2s 则滚动产生一个小文件 每隔2s产生一个小文件 
      .withRolloverInterval(2000) 
    //当文件大小超过256 则滚动产生一个小文件 
      .withMaxPartSize(256*1024*1024) 
      .build() 
    /***
     * 默认： 
     * 每一个小时对应一个桶（文件夹），每一个thread处理的结果对应桶下面的一个小文件
     * 当小文件大小超过128M或者小文件打开时间超过60s,滚动产生第二个小文件 
     */ 
    val sink: StreamingFileSink[String] = StreamingFileSink
      .forRowFormat( new Path("d:/data/rests"), 
        new SimpleStringEncoder[String]("UTF-8")) 
      .withBucketCheckInterval(1000) 
      .withRollingPolicy(rolling) 
      .build() 
    // val sink = StreamingFileSink.forBulkFormat( 
    // new Path("./data/rest"), 
    // ParquetAvroWriters.forSpecificRecord(classOf[String]) 
    // ).build() 
    restStream.addSink(sink)
    
    env.execute()
  }
}
