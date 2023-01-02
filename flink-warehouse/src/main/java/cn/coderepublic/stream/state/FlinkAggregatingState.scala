package cn.coderepublic.stream.state

import cn.coderepublic.stream.state.FlinkValueState.CarInfo
import org.apache.flink.api.common.functions.{AggregateFunction, ReduceFunction, RichMapFunction}
import org.apache.flink.api.common.state.{AggregatingState, AggregatingStateDescriptor, ReducingState, ReducingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

/**
 * @description: 使用 AggregatingState 统计每辆车的速度总和
 * @author: shier
 * @date: 2022/12/31 10:43
 */
object FlinkAggregatingState {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)
    stream.map(data => {
      val splits: Array[String] = data.split(" ")
      CarInfo(splits(0), splits(1).toLong)
    }).keyBy(_.carId)
      .map(new RichMapFunction[CarInfo, CarInfo] {
        private var aggState: AggregatingState[Long, Long] = _

        override def open(parameters: Configuration) = {
          val aggDesc= new AggregatingStateDescriptor[Long, Long, Long]("agg", new AggregateFunction[Long, Long, Long] {
            // 初始化累加器值
            override def createAccumulator() = 0

            // 往累加器中累加值
            override def add(in: Long, acc: Long) = {
              acc + in
            }

            // 返回最终结果
            override def getResult(acc: Long) = {
              acc
            }

            // 合并两个累加器值
            override def merge(acc: Long, acc1: Long) = {
              acc + acc1
            }
          }, createTypeInformation[Long])
          aggState = getRuntimeContext.getAggregatingState(aggDesc)
        }

        override def map(in: CarInfo) = {
          aggState.add(in.speed)
          println("carId:" + in.carId + " speed count:" + aggState.get())
          in
        }
      })

    env.execute()
  }
}
