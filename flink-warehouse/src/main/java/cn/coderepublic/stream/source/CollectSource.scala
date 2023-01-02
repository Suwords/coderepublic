package cn.coderepublic.stream.source

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
/**
 * @description: 从本地集合中读取数据，没有太大意义，多用于测试
 * @author: shier
 * @date: 2022/12/27 10:39
 */
object CollectSource {
  def main(args: Array[String]): Unit = {
    // 使用本地模式演示，并开启WEb UI
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    // 从集合中读取数据
    val collectStream: DataStream[Int] = env.fromCollection(List(1, 2, 3, 4, 5, 6, 7))

    collectStream.print()

    env.execute()
  }
}
