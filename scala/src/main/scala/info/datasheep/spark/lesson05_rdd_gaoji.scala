package info.datasheep.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: shier
 * @date: 2022/10/29 16:12
 */
object lesson05_rdd_gaoji {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local").setAppName("lesson05_rdd_gaoji")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("ERROR")

    val data: RDD[Int] = sc.parallelize(1 to 10, 5)
    println("------------")
    data.sample(true, 0.1, 222).foreach(println)
    println("----相同种子获取的抽样相同-----")
    data.sample(true, 0.1, 222).foreach(println)
    println("----演示不同种子的抽样结果-----")
    data.sample(false, 0.1, 221).foreach(println)

    println(s"data: ${data.getNumPartitions}")

    val data1: RDD[(Int, Int)] = data.mapPartitionsWithIndex(
      (pi, pt) => {
        pt.map(e => (pi, e))
      }
    )
    val repartition: RDD[(Int, Int)] = data1.repartition(8)

    val res01: RDD[(Int, (Int, Int))] = repartition.mapPartitionsWithIndex(
      (pi, pt) => {
        pt.map(e => (pi, e))
      }
    )
    val repartition1: RDD[(Int, Int)] = data1.coalesce(3, false)
    val res02: RDD[(Int, (Int, Int))] = repartition1.mapPartitionsWithIndex(
      (pi, pt) => {
        pt.map(e => (pi, e))
      }
    )

    println(s"data: ${res01.getNumPartitions}")
    println(s"data: ${res02.getNumPartitions}")

    data1.foreach(println)
    println("--------")
    res01.foreach(println)
    println("--------")
    res02.foreach(println)
  }
}
