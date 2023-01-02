package cn.coderepublic.stream.source

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
/**
 * @description: FileSource 算子演示
 * @author: shier
 * @date: 2022/12/27 10:19
 */
object FileSource {
  def main(args: Array[String]): Unit = {
    // 使用本地模式演示，并开启WEb UI
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    // readTextFile读取数据
//    val textStream: DataStream[String] = env.readTextFile("hdfs://nameservice1:8020/flink/data/wc")
    val textStream: DataStream[String] = env.readTextFile("flink-warehouse/src/data/wc")
    textStream.flatMap(_.split(" ")).map((_,1)).keyBy(0).sum(1).print()

    env.execute()
  }
}
