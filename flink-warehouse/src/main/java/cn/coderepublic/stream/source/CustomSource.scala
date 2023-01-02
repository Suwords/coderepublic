package cn.coderepublic.stream.source

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import scala.util.Random

/**
 * @description: 自定义数据源
 * @author: shier
 * @date: 2022/12/27 16:24
 */
object CustomSource {
  def main(args: Array[String]): Unit = {
    // 使用本地模式演示，并开启WEb UI
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    // source 的并行度为1 单并行度source源
    val stream: DataStream[String] = env.addSource(new SourceFunction[String] {
      var flag = true

      override def run(sourceContext: SourceFunction.SourceContext[String]) = {
        val random = new Random()
        while (flag) {
          sourceContext.collect("hello " + random.nextInt(1000))
          Thread.sleep(200)
        }
      }

      // 停止生产数据
      override def cancel() = flag = false
    })

    stream.print()

    env.execute()
  }
}
