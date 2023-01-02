package cn.coderepublic.stream.partition

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @description: Global
 * @author: shier
 * @date: 2022/12/28 17:13
 */
object GlobalPartition {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[Long] = env.generateSequence(1, 10).setParallelism(2)
    stream.writeAsText("flink-warehouse/src/data/stream1").setParallelism(2)
    stream.global.writeAsText("flink-warehouse/src/data/stream2").setParallelism(4)

    env.execute()
  }

}
