package cn.coderepublic.stream.transform

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

/**
 * @description: CoFlatMap 方式三
 * @author: shier
 * @date: 2022/12/28 09:56
 */
object CoFlatMapFunctionTransformation {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val ds1: DataStream[String] = env.socketTextStream("localhost", 8888)
    val ds2: DataStream[String] = env.socketTextStream("localhost", 9999)

    ds1.connect(ds2).flatMap(new CoFlatMapFunction[String, String, (String, Int)] {
      // 对第一个数据流做计算
      override def flatMap1(in1: String, collector: Collector[(String, Int)]) = {
        val words: Array[String] = in1.split(" ")
        words.foreach(x => {collector.collect((x,1))})
      }

      // 对第二个数据流做计算
      override def flatMap2(in2: String, collector: Collector[(String, Int)]) = {
        val words: Array[String] = in2.split(" ")
        words.foreach(x => {collector.collect((x,1))})
      }
    }).print()
    env.execute()
  }

}
