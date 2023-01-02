package cn.coderepublic.stream.source

import org.apache.flink.api.java.io.TextInputFormat
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
 * @description: 通过 readFile 底层算子实现读取文件做为数据源
 * @author: shier
 * @date: 2022/12/27 10:32
 */
object ReadFileSource {
  def main(args: Array[String]): Unit = {
    // 使用本地模式演示，并开启WEb UI
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    // 读取 hdfs 文件
    val filePath = "hdfs://nameservice1:8020/flink/data"
    val textInputFormat = new TextInputFormat(new Path(filePath))
    val textStream: DataStream[String] = env.readFile(textInputFormat, filePath, FileProcessingMode.PROCESS_CONTINUOUSLY, 10)

    textStream.flatMap(_.split("_")).map((_,1)).keyBy(0).sum(1).print()

    env.execute()
  }
}
