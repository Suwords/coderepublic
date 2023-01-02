package cn.coderepublic.stream.state

import cn.coderepublic.stream.state.FlinkValueState.CarInfo
import org.apache.flink.api.common.functions.{ReduceFunction, RichMapFunction}
import org.apache.flink.api.common.state.{ReducingState, ReducingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
/**
 * @description: 使用 Reducingstate 统计每辆车的速度总和
 * @author: shier
 * @date: 2022/12/31 10:43
 */
object FlinkReducingState {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)
    stream.map(data => {
      val splits: Array[String] = data.split(" ")
      CarInfo(splits(0), splits(1).toLong)
    }).keyBy(_.carId)
      .map(new RichMapFunction[CarInfo, CarInfo] {
        private var reducingState: ReducingState[Long] = _


        override def open(parameters: Configuration) = {
          val reduceDesc = new ReducingStateDescriptor[Long]("reducingSpeed", new ReduceFunction[Long] {
            override def reduce(t: Long, t1: Long) = t + t1
          }, createTypeInformation[Long])
          reducingState = getRuntimeContext.getReducingState(reduceDesc)
        }

        override def map(in: CarInfo) = {
          reducingState.add(in.speed)
          println("carId:" + in.carId + " speed count:" + reducingState.get())
          in
        }
      })

    env.execute()
  }
}
