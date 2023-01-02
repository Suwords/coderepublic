package cn.coderepublic.stream.partition

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @description: shuffle 分区策略
 * @author: shier
 * @date: 2022/12/28 17:09
 */
object ShufflePartition {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[Long] = env.generateSequence(1, 10).setParallelism(1)
    println(stream.getParallelism)
    stream.shuffle.print()

    env.execute()
  }
}
