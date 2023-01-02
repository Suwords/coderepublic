package cn.coderepublic.stream.transform

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
/**
 * @description: Connect 算子
 * @author: shier
 * @date: 2022/12/28 09:56
 */
object ConnectTransformation {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val ds1: DataStream[String] = env.socketTextStream("cdh001", 8888)
    val ds2: DataStream[String] = env.socketTextStream("cdh001", 9999)

    val wcStream1: DataStream[(String, Int)] = ds1.flatMap(_.split(" ")).map((_, 1)).keyBy(0).sum(1)
    val wcStream2: DataStream[(String, Int)] = ds2.flatMap(_.split(" ")).map((_, 1)).keyBy(0).sum(1)

    val restStream: ConnectedStreams[(String, Int), (String, Int)] = wcStream2.connect(wcStream1)

    env.execute()
  }

}
