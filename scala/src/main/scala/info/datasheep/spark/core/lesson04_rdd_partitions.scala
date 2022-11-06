package info.datasheep.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

/**
 * @description:
 * @author: shier
 * @date: 2022/10/29 15:58
 */
object lesson04_rdd_partitions {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("lesson04_rdd_partitions")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("ERROR")

    val data: RDD[Int] = sc.parallelize(1 to 10, 2)

    // 外关联 SQL查询
    val res01: RDD[String] = data.map(
      (value: Int) => {
        println("------conn--mysql----")
        println(s"-----select $value----")
        println("------close--mysql-----")
        value + "selected"
      }
    )
    res01.foreach(println)

    println("===================")

    val res02: RDD[String] = data.mapPartitionsWithIndex(
      (pindex, piter) => {
        val lb = new ListBuffer[String] // 致命的！！！ 根据之前的源码发现 Spark就是一个pipeline，迭代器嵌套的模式
        // 数据不会在内存中积压
        println(s"----$pindex---conn--mysql-----")
        while (piter.hasNext) {
          val value: Int = piter.next()
          println(s"----pindex--select $value----")
          lb.+=(value + "selected")
        }
        println("-----close--mysql-------")
        lb.iterator
      }
    )
    res02.foreach(println)

    println("====iterator========")
    val res03: RDD[String] = data.mapPartitionsWithIndex(
      (pindex, piter) => {
        new Iterator[String] {
          println(s"----$pindex--conn---mysql----")

          override def hasNext = if (piter.hasNext == false) {
            println(s"----$pindex--close--mysql---");
            false
          } else true

          override def next() = {
            val value: Int = piter.next()
            println(s"---$pindex--select $value-----")
            value + "selected"
          }
        }
      }
    )
    res03.foreach(println)
  }
}
