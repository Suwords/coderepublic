package cn.coderepublic.stream.transform

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
/**
 * @description: Iterate 算子
 * @author: shier
 * @date: 2022/12/28 14:19
 */
object IterateTransformation {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val initStream: DataStream[String] = env.socketTextStream("localhost", 8888)
    val stream: DataStream[Long] = initStream.map(_.toLong)
    stream.iterate {
      iteration => {
        // 定义迭代逻辑
        val iterationBody: DataStream[Long] = iteration.map(x => {
          println(x)
          if (x > 0) x - 1
          else x
        })
        // >0 大于0的值继续返回stream流中，<=0 继续往下游发送
        (iterationBody.filter(_ > 0), iterationBody.filter(_ <= 0))
      }
    }.print()

    env.execute()
  }

}
