package cn.coderepublic.stream.transform

import org.apache.flink.api.common.functions.{FilterFunction, RichMapFunction}
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import redis.clients.jedis.Jedis

/**
 * @description: 使用富函数类，将车牌号转化成车主真实姓名，映射表存储在 Redis 中
 * @author: shier
 * @date: 2022/12/28 14:19
 */
object TrafficCarID2Name {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)
    stream.map(new RichMapFunction[String, String] {

      private var jedis: Jedis = _

      // 初始化函数 在每一个 thread 启动时（处理元素时） 调用一次
      // 在 open 中可以创建 redis 的连接
      override def open(parameters: Configuration) = {
        // getRuntimeContext 可以获取 flink 运行的上下文环境 AbstractRichFunction 抽象类提供
        val taskName: String = getRuntimeContext.getTaskName
        val subtasks: String = getRuntimeContext.getTaskNameWithSubtasks

        println("=====open====taskName:" + taskName +"\tsubtasks:" + subtasks)

        jedis = new Jedis("localhost", 6379)
        jedis.select(3)
      }

      // 元素处理完毕后 调用 close
      override def close() = {
        // 关闭redis连接
        jedis.close()
      }

      // 每处理一个元素 就会调用一次
      override def map(in: String) = {
        val name: String = jedis.get(in)
        if (name == null) {
          "not found name"
        } else name
      }
    }).setParallelism(2).print()

    env.execute()
  }

}
