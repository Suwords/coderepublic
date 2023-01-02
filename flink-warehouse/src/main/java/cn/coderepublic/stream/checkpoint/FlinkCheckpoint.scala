package cn.coderepublic.stream.checkpoint

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, createTypeInformation}

/**
 * @description: checkpoint & savepoint
 * @author: shier
 * @date: 2022/12/20 21:29
 */
object FlinkCheckpoint {
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    // 升级前代码
//    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)
//    stream.flatMap(_.split(" ")).uid("001")
//      .map((_, 1)).uid("002")
//      .print()

    // 升级后代码
    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)
    stream.flatMap(_.split(" ")).uid("001")
      .map((_, 1)).uid("002")
      .keyBy(_._1)
      .sum(1).uid("003")
      .print()

    // 启动任务
    env.execute("word count")
  }
}
