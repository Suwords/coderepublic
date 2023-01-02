package cn.coderepublic.stream.partition

import org.apache.flink.api.common.functions.Partitioner
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
/**
 * @description: 自定义分区器
 * @author: shier
 * @date: 2022/12/28 17:13
 */
object CustomPartition {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)

    val stream = env.generateSequence(1, 10).map((_,1))
    stream.writeAsText("flink-warehouse/src/data/stream1").setParallelism(2)
    stream.partitionCustom(new customPartitioner, 0).writeAsText("flink-warehouse/src/data/stream2").setParallelism(4)

    env.execute()
  }

  class customPartitioner extends Partitioner[Long]{
    override def partition(k: Long, i: Int): Int = {
      k.toInt % i
    }
  }
}

