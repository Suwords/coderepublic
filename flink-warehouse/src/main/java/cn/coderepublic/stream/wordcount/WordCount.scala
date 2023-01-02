package cn.coderepublic.stream.wordcount

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, createTypeInformation}

/**
 * @description:
 * @author: shier
 * @date: 2022/12/20 21:29
 */
object WordCount {
  def main(args: Array[String]): Unit = {

    /**
     * 获取运行环境
     * createLocalEnvironment 创建一个本地环境 local
     * createLocalEnvironmentWithWebUI 创建一个本地环境 同时开启WEB UI
     * getExecutionEnvironment 根据你的执行环境创建上下文，如local cluster
    */
//    val conf = new Configuration()
//    import org.apache.flink.configuration.ConfigConstants
//    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
//    conf.setInteger(RestOptions.PORT, 8081)
//    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment
      .getExecutionEnvironment
    env.setParallelism(1)
    /**
     * DataStream：一组相同类型的元素组成的数据流
     * 如果数据源是socket 并行度只能是1
    */
    val socketStream: DataStream[String] = env
      .socketTextStream("cdh001", 12222)
    val wordsStream: DataStream[String] = socketStream
      .flatMap(_.split(" ")).setParallelism(2)
    val pairStream: DataStream[(String, Int)] = wordsStream.map((_, 1)).setParallelism(2)
    val keyByStream: KeyedStream[(String, Int), Tuple] = pairStream.keyBy(0)
    val sumStream: DataStream[(String, Int)] = keyByStream.sum(1).setParallelism(2)
    sumStream.print()

    /** 默认就是有状态的计算 7> 代表是哪个线程处理的 相同的数据一定是同一个thread处理的
      7> (flink,1)
      1> (spark,1)
      8> (hadoop,1)
      3> (hello,1)
      2> (where,1)
      4> (are,1)
      5> (you,1)
      4> (hbase,1)
      3> (hello,2)
     */
    // 启动任务
    env.execute("word count")
  }
}
