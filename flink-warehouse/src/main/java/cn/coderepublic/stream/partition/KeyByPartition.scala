package cn.coderepublic.stream.partition

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @description: keyBy
 * @author: shier
 * @date: 2022/12/28 17:13
 */
object KeyByPartition {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[Long] = env.generateSequence(1, 10).setParallelism(2)
    stream.writeAsText("flink-warehouse/src/data/stream1").setParallelism(2)
    stream.keyBy(0).writeAsText("flink-warehouse/src/data/stream2").setParallelism(2)

    env.execute()
  }

}
