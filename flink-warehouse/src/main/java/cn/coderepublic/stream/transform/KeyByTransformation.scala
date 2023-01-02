package cn.coderepublic.stream.transform

import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

/**
 * @description: KeyBy算子
 * @author: shier
 * @date: 2022/12/28 09:08
 */
object KeyByTransformation {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    // 生成队列数据源
    val stream: DataStream[Long] = env.generateSequence(1, 100)

    stream.map( x => (x % 3, 1))
      // 根据索引号指定分区字段
      // .keyBy(0)
      // 通过传入匿名函数 指定分区字段
      // .keyBy(x => x._1)
      // 通过实现 KeySelector 接口 指定分区字段
      .keyBy(new KeySelector[(Long, Int), Long] {
        override def getKey(in: (Long, Int)) = in._1
      })
      .sum(1)
      .print()

    env.execute()
  }
}
