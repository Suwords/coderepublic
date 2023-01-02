package cn.coderepublic.stream.transform

import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

import java.io

/**
 * @description: Union 算子
 * @author: shier
 * @date: 2022/12/28 09:51
 */
object UnionTransformation {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val ds1: DataStream[io.Serializable] = env.fromCollection(List(("a", 1), ("b",2), ("c", 3)))
    val ds2: DataStream[io.Serializable] = env.fromCollection(List(("d", 4), ("e",5), ("f", 6)))
    val ds3: DataStream[io.Serializable] = env.fromCollection(List(("g", 7), ("h", 8)))

    val unionStream: DataStream[io.Serializable] = ds1.union(ds2, ds3)
    unionStream.print()

    env.execute()
  }
}
