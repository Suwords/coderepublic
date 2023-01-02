package cn.coderepublic.stream.transform

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

import java.text.SimpleDateFormat
import java.util.Date

/**
 * @description: side output 侧输出流
 * @author: shier
 * @date: 2022/12/28 09:44
 */
object SideOutputTransformation {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)

    // 定义侧输出标签
    val gtTag = new OutputTag[String]("gt")
    val processStream: DataStream[String] = stream.process(new ProcessFunction[String, String] {
      override def processElement(i: String, context: ProcessFunction[String, String]#Context, collector: Collector[String]) = {
        val longVar: Long = i.toLong
        if (longVar > 100) {
          collector.collect(i)
        } else {
          context.output(gtTag, i)
        }
      }
    })

    val sideStream: DataStream[String] = processStream.getSideOutput(gtTag)

    sideStream.print("sideStream")
    processStream.print("mainStream")

    env.execute()
  }

}
