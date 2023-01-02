package cn.coderepublic.stream.transform

import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

/**
 * @description: 使用普通函数类过滤车速高于100的车辆信息
 * @author: shier
 * @date: 2022/12/28 14:19
 */
object TrafficeSpeedFilter {
  def main(args: Array[String]): Unit = {
    val conf = new Configuration()
    import org.apache.flink.configuration.ConfigConstants
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
    conf.setInteger(RestOptions.PORT, 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    val stream: DataStream[String] = env.readTextFile("flink-warehouse/src/data/carFlow_all_column_test.txt")
    stream.filter(new FilterFunction[String] {
      override def filter(t: String) = {
        if (t != null && !"".equals(t)) {
          val speed = t.split(",")(6).replace("'","").toLong
          if (speed > 100) false else true
        } else false
      }
    }).print()

    env.execute()
  }

}
