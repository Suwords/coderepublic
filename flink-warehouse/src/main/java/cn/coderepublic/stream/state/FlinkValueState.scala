package cn.coderepublic.stream.state

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
/**
 * @description: 使用 ValueState Keyed State 检查车辆是否发生了急加速
 * @author: shier
 * @date: 2022/12/31 10:21
 */
object FlinkValueState {
  case class CarInfo(carId: String, speed: Long)
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)

    stream.map(data => {
      val arr: Array[String] = data.split(" ")
      CarInfo(arr(0), arr(1).toLong)
    }).keyBy(_.carId)
      .map(new RichMapFunction[CarInfo, String] {
        // 保存上一次车速
        private var lastTempState: ValueState[Long] = _

        override def open(parameters: Configuration) = {
          val lastTempStateDesc = new ValueStateDescriptor[Long]("lastTempState", createTypeInformation[Long])
          lastTempState = getRuntimeContext.getState(lastTempStateDesc)
        }

        override def map(in: CarInfo) = {
          val lastSpeed = lastTempState.value()
          this.lastTempState.update(in.speed)
          if((in.speed - lastSpeed).abs > 30 && lastSpeed != 0){
            "over speed" + in.toString
          } else {
            in.carId
          }
        }
      }).print()

    env.execute()
  }
}
