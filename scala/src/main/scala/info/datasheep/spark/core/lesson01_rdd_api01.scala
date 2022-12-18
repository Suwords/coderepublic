package info.datasheep.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * 面向数据集操作：
 * *. 带函数的非聚合：map、flatMap
 * 1. 单元素：union、cartesion 没有函数计算
 * 2. kv元素：cogroup、join 没有函数计算
 * 3. 排序
 * 4. 聚合计算：reduceByKey 有函数 combinerByKey
 * 核心算子是：cogroup、combinerByKey
 * @author: shier
 * @date: 2022/10/24 21:40
 */
object lesson01_rdd_api01 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("lesson01_rdd_api01")
    val sparkContext = new SparkContext(sparkConf)
    sparkContext.setLogLevel("ERROR")

    val dataRDD: RDD[Int] = sparkContext.parallelize(List(1, 2, 3, 4, 5, 4, 3, 2, 1))

    println("============*. 带函数的非聚合操作 ==========")
//    dataRDD.map(_ * 2).foreach(println)
//    println("-----------------------")
//    dataRDD.flatMap(List(_, 1)).foreach(println)
//    println("-----------------------")
//    dataRDD.filter(_ > 3).foreach(println)
//    println("-----------------------")

    // 通过 reduceByKey 进行去重
//    val res01: RDD[Int] = dataRDD.map((_, 1)).reduceByKey(_ + _).map(_._1)
//    res01.foreach(println)
//    println("-----------------------")
//    // 直接使用去重算子
//    val res02: RDD[Int] = dataRDD.distinct() // map(x => (x, null)).reduceByKey((x, y) => x, numPartitions).map(_._1)
//    res02.foreach(println)
//    println("-----------------------")
    // 面向数据集开发，面向数据集的API 1. 基础API 2. 复合API
    // RDD HadoopRDD MappartitionsRDD ShuffledRDD
    // map flatMap filter
    // distinct
    // reduceByKey：复合 -> combineByKey

    // 面向数据集：交并集 关联 笛卡尔积
    // 面向数据集：元素 --》单元素，K,V元素 -》结构化、非结构化

    // spark很人性，面向数据集提供了不同的方法的封装，且方法经过检验，常识，推断出自己的实现方式
    // 人不需要干预（会有一个算子）
    val rdd01: RDD[Int] = sparkContext.parallelize(List(1, 2, 3, 4, 5))
    val rdd02: RDD[Int] = sparkContext.parallelize(List(3, 4, 5, 6, 7))
//
//    println("=============差集：提供了一个方法，有方向的==============")
//    val subtractRDD: RDD[Int] = rdd01.subtract(rdd02)
//    subtractRDD.foreach(println)
//
//    println("=============交集==============")
//    val intersectionRDD: RDD[Int] = rdd01.intersection(rdd02)
//    intersectionRDD.foreach(println)
//
//    println("===========笛卡尔积=============")
//    // 如果数据，不需要区分每一条记录归属与哪个分区，间接的，这样的数据不需要partitioner，不需要shuffle
//    // 因为shuffle的语义：洗牌  ---》 面向每一条记录计算它的分区号
//    // 如果有行为，不需要区分记录，本地IO拉取数据，那么这种直接IO一定比先partition计算，shuffle落文件，最后再IO拉取速度快！！！
//    val cartesianRDD: RDD[(Int, Int)] = rdd01.cartesian(rdd02)
//    cartesianRDD.foreach(println)
//
//    println("-----------------------------")
//    println(rdd01.partitions.size)
//    println(rdd02.partitions.size)
//
//    val unionRDD: RDD[Int] = rdd01.union(rdd02)
//    unionRDD.foreach(println)
//    println(unionRDD.partitions.size)
//
//    println("========================")
    val kv1: RDD[(String, Int)] = sparkContext.parallelize(List(
      ("zhangsan", 11),
      ("zhangsan", 12),
      ("lisi", 13),
      ("wangwu", 14)
    ))
    val kv2: RDD[(String, Int)] = sparkContext.parallelize(List(
      ("zhangsan", 21),
      ("zhangsan", 22),
      ("lisi", 23),
      ("zhaoliu", 28)
    ))
//
//    val cogroup: RDD[(String, (Iterable[Int], Iterable[Int]))] = kv1.cogroup(kv2)
//    cogroup.foreach(println)
//
//    println("----------------")
//
//    val joinRDD: RDD[(String, (Int, Int))] = kv1.join(kv2)
//    joinRDD.foreach(println)
//
//    println("----------------")
//
//    val leftRDD: RDD[(String, (Int, Option[Int]))] = kv1.leftOuterJoin(kv2)
//    leftRDD.foreach(println)
//
//    println("----------------")
//
//    val rightRDD: RDD[(String, (Option[Int], Int))] = kv1.rightOuterJoin(kv2)
//    rightRDD.foreach(println)
//
//    println("----------------")
//
    val fullRDD: RDD[(String, (Option[Int], Option[Int]))] = kv1.fullOuterJoin(kv2)
    fullRDD.foreach(println)

    while (true) {

    }
  }
}
