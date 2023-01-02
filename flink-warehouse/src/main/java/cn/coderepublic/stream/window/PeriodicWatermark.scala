package cn.coderepublic.stream.window

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * @description: 周期性水印
 * @author: shier
 * @date: 2023/1/2 13:08
 */
object PeriodicWatermark {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    env.getConfig.setAutoWatermarkInterval(100)
    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[String](Time.seconds(3)) {
        override def extractTimestamp(t: String) = {
          t.split(" ")(0).toLong
        }
      })

    env.execute()
  }
}
