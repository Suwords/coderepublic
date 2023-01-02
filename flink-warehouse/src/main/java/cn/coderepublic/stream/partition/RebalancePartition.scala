package cn.coderepublic.stream.partition

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @description: rebalance 分区策略
 * @author: shier
 * @date: 2022/12/28 17:13
 */
object RebalancePartition {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(3)

    val stream: DataStream[Long] = env.generateSequence(1, 100)
    val rebalanceStream: DataStream[Long] = stream.rebalance

    rebalanceStream.print()

    env.execute()
  }

}
