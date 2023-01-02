package cn.coderepublic.stream.transform

import org.apache.flink.api.java.io.TextInputFormat
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.co.{CoFlatMapFunction, CoMapFunction}
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

import java.util
import scala.collection.mutable

/**
 * @description: 现有一个配置文件存储车牌号与车主的真是姓名，通过数据流中的车牌号实时匹配出对应的车主姓名（注意：配置文件可能实时改变）
 * @author: shier
 * @date: 2022/12/28 09:56
 */
object TrafficTransformation {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val filePath = "flink-warehouse/src/data/carId2Name"
    val carId2NameStream = env.readFile(new TextInputFormat(new Path(filePath)), filePath, FileProcessingMode.PROCESS_CONTINUOUSLY, 10)

    val dataStream: DataStream[String] = env.socketTextStream("localhost", 8888)

    dataStream.connect(carId2NameStream).map(new CoMapFunction[String, String, String] {
      private val hashMap = new mutable.HashMap[String, String]()
      override def map1(in1: String) = {
        hashMap.getOrElse(in1, "not found name")
      }

      override def map2(in2: String) = {
        val splits: Array[String] = in2.split(" ")
        hashMap.put(splits(0), splits(1))
        in2 + "加载完毕~~~"
      }
    }).print()

    env.execute()
  }

}
