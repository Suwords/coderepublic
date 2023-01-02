package cn.coderepublic.stream.tablesql

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.api._
import org.apache.flink.table.descriptors.TableDescriptor
/**
 * @description: TableAPI 案例
 * @author: shier
 * @date: 2023/1/2 14:09
 */
object FlinkTableAPI {
  case class CarInfo(carId: String, speed: Long)
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    // 基于 env 创建 tableEnv
    val settings: EnvironmentSettings = EnvironmentSettings
      .newInstance()
      .inStreamingMode()
      .build()
    val tableEnv: TableEnvironment = TableEnvironment.create(settings)

    // 从一条流创建一张表
    tableEnv.createT("sourceTable", TableDescriptor.for)
    // 从表里选取数据
    val selectedTable: Table = dataTable.select("carId")
      .filter("carId='001'")

    selectedTable.t


    env.execute()

  }
}
