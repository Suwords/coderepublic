package cn.coderepublic.stream.state

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.functions.{RichFlatMapFunction, RichMapFunction}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

import java.text.SimpleDateFormat
import scala.collection.JavaConverters.iterableAsScalaIterableConverter

/**
 * @description: 自系统启动以来，总共处理了多少条数据量
 * @author: shier
 * @date: 2022/12/31 10:43
 */
object FlatMapOperatorState {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val stream = env.fromCollection(List("I love you", "hello spark", "hello hadoop"))
    stream.map(data => {
      (data, 1)
    }).keyBy(_._1)
      .flatMap(new MyFlatMapFunction())

    env.execute()
  }

  class MyFlatMapFunction extends RichFlatMapFunction[(String, Int), (String, Int, Int)] with CheckpointedFunction{
    private var operatorCount: Long = _
    private var operatorState: ListState[Long] = _

    override def flatMap(in: (String, Int), collector: Collector[(String, Int, Int)]): Unit = {
      operatorCount += 1
      val subtasks: String = getRuntimeContext.getTaskNameWithSubtasks
      println(subtasks + "==" + operatorState.get())
    }

    // 进行Checkpoint时会被调用，然后持久化到远端
    override def snapshotState(functionSnapshotContext: FunctionSnapshotContext): Unit = {
      operatorState.clear()
      operatorState.add(operatorCount)
    }

    // 初始化方法
    override def initializeState(context: FunctionInitializationContext): Unit = {
      operatorState = context.getOperatorStateStore.getListState(new ListStateDescriptor[Long]("operateState", createTypeInformation[Long]))
      if (context.isRestored) {
        operatorCount = operatorState.get().asScala.sum
      }
    }
  }
}
