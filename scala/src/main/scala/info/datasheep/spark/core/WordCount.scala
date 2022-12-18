package info.datasheep.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: shier
 * @date: 2022/10/22 10:23
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    // 准备运行环境
    val sparkConf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)

    // 加载数据集
    val dataRDD: RDD[String] = sparkContext.textFile("./scala/data/wordcount.txt")

    //dataRDD.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).foreach(println)

    // 分割数据
    val wordsRDD: RDD[String] = dataRDD.flatMap(_.split(" "))

    // 组合成tuple2
    val wordRDD: RDD[(String, Int)] = wordsRDD.map((_, 1))

    // 聚合计算
    val result: RDD[(String, Int)] = wordRDD.reduceByKey(_ + _)

    // 遍历打印
    result.foreach(println)

    // 迭代计算
    result.map((x) => {
      (x._2, 1)
    }).reduceByKey(_ + _).foreach(println)

    while(true) {

    }
  }

}
