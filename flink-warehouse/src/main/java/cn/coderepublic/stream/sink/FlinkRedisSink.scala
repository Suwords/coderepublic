package cn.coderepublic.stream.sink

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.{FlinkJedisClusterConfig, FlinkJedisPoolConfig}
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

import java.net.InetSocketAddress
import java.util

/**
 * @description: WordCount 写入到 Redis 中，选择的是 HSET 数据类型
 * @author: shier
 * @date: 2022/12/28 21:01
 */
object FlinkRedisSink {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[String] = env.socketTextStream("localhost", 8888)
    val result: DataStream[(String, Int)] = stream.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(0)
      .sum(1)

    // 若 redis 是单机
    val config: FlinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder()
      .setDatabase(3)
      .setHost("localhost")
      .setPort(6379)
      .build()

    // 若 redis 是集群
//    val addresses = new util.HashSet[InetSocketAddress]()
//    addresses.add(new InetSocketAddress("localhost", 6379))
//    val clusterConfig = new FlinkJedisClusterConfig.Builder().setNodes(addresses).build()

    result.addSink(new RedisSink[(String, Int)](config, new RedisMapper[(String, Int)] {
      override def getCommandDescription = {
        new RedisCommandDescription(RedisCommand.HSET, "wc")
      }

      override def getKeyFromData(t: (String, Int)) = {
        t._1
      }

      override def getValueFromData(t: (String, Int)) = {
        t._2 + ""
      }
    }))

    env.execute()
  }
}
