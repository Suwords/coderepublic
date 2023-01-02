package cn.coderepublic.stream.transform

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.functions.co.CoMapFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

/**
 * @description: CoFlatMap 方式一
 * @author: shier
 * @date: 2022/12/28 09:56
 */
object CoFlatMapTransformation {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val ds1: DataStream[String] = env.socketTextStream("localhost", 8888)
    val ds2: DataStream[String] = env.socketTextStream("localhost", 9999)

    ds1.connect(ds2).flatMap(
      (x, c: Collector[String]) => {
        // 对第一个数据流做计算
        x.split(" ").foreach(c.collect(_))
      },
      // 对第二个数据流做计算
      (y, c:Collector[String]) => {
        y.split(" ").foreach(c.collect(_))
      }
    ).print()
    env.execute()
  }

}
