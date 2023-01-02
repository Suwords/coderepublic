package cn.coderepublic.stream.source

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.functions.source.{ParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import scala.util.Random

/**
 * @description: 实现多并行度数据源
 * @author: shier
 * @date: 2022/12/27 16:30
 */
object ParallelCustomSource {
  def main(args: Array[String]): Unit = {
    // 使用本地模式演示，并开启WEb UI
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val stream: DataStream[String] = env.addSource(new ParallelSourceFunction[String] {
      var flag = true

      override def run(sourceContext: SourceFunction.SourceContext[String]) = {
        val random = new Random()
        while (flag) {
          sourceContext.collect("hello " + random.nextInt(1000))
          Thread.sleep(200)
        }
      }

      override def cancel() = flag = false
    }).setParallelism(2)

    stream.print()

    env.execute()
  }
}
