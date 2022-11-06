package info.datasheep.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: shier
 * @date: 2022/10/25 22:13
 */
object lesson03_rdd_aggregator {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("lesson03_rdd_aggregator")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("ERROR")

    val data: RDD[(String, Int)] = sc.parallelize(List(
      ("zhangsan", 234),
      ("zhangsan", 5667),
      ("zhangsan", 343),
      ("lisi", 212),
      ("lisi", 44),
      ("lisi", 33),
      ("wangwu", 535),
      ("wangwu", 22)
    ))

    // key value -> 一组
    val group: RDD[(String, Iterable[Int])] = data.groupByKey()
    group.foreach(println)

    // 行列转换
    val res01: RDD[(String, Int)] = group.flatMap(x => x._2.map(i => (x._1, i)).iterator)
    res01.foreach(println)

    println("-------------")
    group.flatMapValues(e => e.iterator).foreach(println)

    println("-------------")
    group.mapValues(e => e.toList.sorted.take(2)).foreach(println)

    println("-------------")
    group.flatMapValues(e => e.toList.sorted.take(2)).foreach(println)

    println("---------max,min,count,sum,avg-----------")
    val sum: RDD[(String, Int)] = data.reduceByKey(_ + _)
    val max: RDD[(String, Int)] = data.reduceByKey((oldv, newv) => if (oldv > newv) oldv else newv)
    val min: RDD[(String, Int)] = data.reduceByKey((oldv, newv) => if (oldv > newv) newv else oldv)
    val count: RDD[(String, Int)] = data.mapValues(e => 1).reduceByKey(_ + _)
    val temp: RDD[(String, (Int, Int))] = sum.join(count)
    val avg: RDD[(String, Int)] = temp.mapValues(e => e._1 / e._2)
    println("-----sum-----")
    sum.foreach(println)
    println("-----max-----")
    max.foreach(println)
    println("-----min-----")
    min.foreach(println)
    println("----count----")
    count.foreach(println)
    println("----avg-----")
    avg.foreach(println)

    println("------------avg--combine----------")
    val tmp: RDD[(String, (Int, Int))] = data.combineByKey(
      // createCombiner: V => C,
      // 第一条记录的 value 怎么放入hashmap
      (value: Int) => (value, 1),
      // mergeValue: (C, V) => C,
      //如果有第二条记录，第二条以及以后的他们的value怎么放到hashmap里
      (oldv: (Int, Int), newv: Int) => (oldv._1 + newv, oldv._2 + 1),
      // mergeCombiners: (C, C) => C,
      //合并溢写结果的函数
      (v1: (Int, Int), v2: (Int, Int)) => (v1._1 + v2._1, v1._2 + v2._2)
    )

    tmp.mapValues(e => e._1 / e._2).foreach(println)
  }

}
