package cn.coderepublic.stream.transform

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.functions.co.CoMapFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

/**
 * @description: CoMap 操作方式一
 * @author: shier
 * @date: 2022/12/28 09:56
 */
object CoMapFunctionTransformation {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val ds1: DataStream[String] = env.socketTextStream("localhost", 8888)
    val ds2: DataStream[String] = env.socketTextStream("localhost", 9999)

    val wcStream1: DataStream[(String, Int)] = ds1.flatMap(_.split(" ")).map((_, 1)).keyBy(0).sum(1)
    val wcStream2: DataStream[(String, Int)] = ds2.flatMap(_.split(" ")).map((_, 1)).keyBy(0).sum(1)

    val restStream: ConnectedStreams[(String, Int), (String, Int)] = wcStream2.connect(wcStream1)

    restStream.map(new CoMapFunction[(String, Int), (String, Int), (String, Int)] {
      // 对第一个数据流做计算 wcStream2
      override def map1(in1: (String, Int)) = {
        (in1._1 + ":first", in1._2+100)
      }

      // 对第二个数据流做计算 wcStream1
      override def map2(in2: (String, Int)) = {
        (in2._1 + ":second", in2._2*100)
      }
    }).print()
    env.execute()
  }

}
