package cn.coderepublic.stream.state

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
/**
 * @description: 使用 MapState 统计单词出现次数
 * @author: shier
 * @date: 2022/12/31 10:31
 */
object FlinkMapState {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[String] = env.fromCollection(List("I love you", "hello spark", "hello flink"))
    val pairStream: KeyedStream[(String, Int), String] = stream.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(_._1)

    pairStream.map(new RichMapFunction[(String, Int), (String, Int)] {
      private var mapState: MapState[String, Int] = _

      override def open(parameters: Configuration) = {
        // 定义 mapstate存储的数据类型
        val desc = new MapStateDescriptor[String, Int]("sum", createTypeInformation[String], createTypeInformation[Int])
        // 注册mapstate
        mapState = getRuntimeContext.getMapState(desc)
      }

      override def map(in: (String, Int)) = {
        val key = in._1
        val v = in._2
        if (mapState.contains(key)){
          mapState.put(key, mapState.get(key) + 1)
        } else {
          mapState.put(key, 1)
          val interator = mapState.keys().iterator()
          while (interator.hasNext) {
            val key = interator.next()
            println("word:" + key + "\tcount:" + mapState.get(key))
          }
        }
        in
      }
    }).setParallelism(3)

    env.execute()
  }
}
