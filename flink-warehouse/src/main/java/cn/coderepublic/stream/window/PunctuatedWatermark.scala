package cn.coderepublic.stream.window

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
/**
 * @description: 间歇性水印生成器
 * @author: shier
 * @date: 2023/1/2 13:16
 */
object PunctuatedWatermark {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // 卡口号 时间戳
    env.socketTextStream("localhost", 8888)
      .map(data => {
        val splits: Array[String] = data.split(" ")
        (splits(0), splits(1).toLong)
      }).assignTimestampsAndWatermarks(new myWatermark(3000))
      .keyBy(_._1)
      .timeWindow(Time.seconds(5))
      .reduce((v1: (String, Long), v2: (String, Long)) => {
        (v1._1 + "," + v2._1, v1._2 + v2._2)
      }).print()

    env.execute()
  }

  class myWatermark(delay: Long) extends AssignerWithPunctuatedWatermarks[(String, Long)] {
    var maxTimeStamp: Long = _
    override def checkAndGetNextWatermark(t: (String, Long), l: Long): Watermark = {
      maxTimeStamp = l.max(maxTimeStamp)
      if ("001".equals(t._1)) {
        new Watermark(maxTimeStamp - delay)
      } else {
        new Watermark(maxTimeStamp)
      }
    }

    override def extractTimestamp(t: (String, Long), l: Long): Long = {
      t._2
    }
  }
}
