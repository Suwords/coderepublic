package cn.coderepublic.stream.source

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
 * @description: 套接字（Socket）数据源演示
 * @author: shier
 * @date: 2022/12/27 10:48
 */
object SocketSource {
  def main(args: Array[String]): Unit = {
    // 使用本地模式演示，并开启WEb UI
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    // 从 Socket 获取数据
    val socketStream: DataStream[String] = env.socketTextStream("localhost", 8888)
    socketStream.print()

    env.execute()
  }
}
