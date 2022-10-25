package info.datasheep.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: shier
 * @date: 2022/10/25 21:40
 */
object lesson02_rdd_api_sort {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("lesson02_rdd_api_sort")
    val sparkContext = new SparkContext(sparkConf)
    sparkContext.setLogLevel("ERROR")

    // PV,UV
    // 需求，根据数据计算各网站的PV和UV，同时只显示TOP5
    // 解题：要按PV值或UV值排序，取前5名
    val fileRDD: RDD[String] = sparkContext.textFile("./scala/data/pvuvdata.txt")

    // PV:
    // 43.169.217.152	河北	2018-11-12	1542011088714	3292380437528494072	www.dangdang.com	Login
    println("--------PV--------")

    val pairRDD: RDD[(String, Int)] = fileRDD.map(line => (line.split("\t")(5), 1))
    val countRDD: RDD[(String, Int)] = pairRDD.reduceByKey(_ + _)
    val swapRDD: RDD[(Int, String)] = countRDD.map(_.swap)
    val sortedRDD: RDD[(Int, String)] = swapRDD.sortByKey(false)
    val res: Array[(String, Int)] = sortedRDD.map(_.swap).take(5)
    res.foreach(println)


    // UV:
    // 43.169.217.152	河北	2018-11-12	1542011088714	3292380437528494072	www.dangdang.com	Login
    println("--------------UV--------------")
    val arrRDD: RDD[(String, String)] = fileRDD.map(line => {
      val arrs: Array[String] = line.split("\t")
      (arrs(5), arrs(0))
    })

    val arr: RDD[(String, String)] = arrRDD.distinct()
    val pairxRDD: RDD[(String, Int)] = arr.map(x => (x._1, 1))
    val resRDD: RDD[(String, Int)] = pairxRDD.reduceByKey(_ + _)
    val sort: RDD[(String, Int)] = resRDD.sortBy(_._2, false)
    val uv: Array[(String, Int)] = sort.take(5)
    uv.foreach(println)

    while (true){

    }

  }
}
