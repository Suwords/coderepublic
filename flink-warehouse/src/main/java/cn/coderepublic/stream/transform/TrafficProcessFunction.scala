package cn.coderepublic.stream.transform

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector
import redis.clients.jedis.Jedis

/**
 * @description: TrafficCarID2Name$
 * @author: shier
 * @date: 2022/12/28 14:19
 */
object TrafficProcessFunction {
  case class CarInfo(carId: String, speed: Long)
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)

    stream.map(data => {
      val splits: Array[String] = data.split(" ")
      val carId = splits(0)
      val speed = splits(1).toLong
      CarInfo(carId, speed)
    }).keyBy(_.carId)
    // KeyedStream 调用process需要传入KeyedProcessFunction
    // DataStream 调用process需要传入ProcessFunction
      .process(new KeyedProcessFunction[String, CarInfo, String] {
        override def processElement(i: CarInfo, context: KeyedProcessFunction[String, CarInfo, String]#Context, collector: Collector[String]) = {
          val currentTime = context.timerService().currentProcessingTime()
          if (i.speed > 100){
            val timerTime = currentTime + 2 * 1000
            context.timerService().registerProcessingTimeTimer(timerTime)
          }
        }

        override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, CarInfo, String]#OnTimerContext, out: Collector[String]) = {
          var warnMsg = "warn... time:" + timestamp + " carId:" + ctx.getCurrentKey
          out.collect(warnMsg)
        }
      }).print()

    env.execute()
  }

}
