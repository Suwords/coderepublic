package cn.coderepublic.wordcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description: WordCount 单词计数
 * @author: shier
 * @date: 2023/1/29 08:33
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    // 1. 创建 SparkConf
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WC")

    // 2. 创建 SparkContext
    val sc = new SparkContext(sparkConf)

    // 3. 读取指定位置路径
    val lineRDD: RDD[String] = sc.textFile("spark-warehouse/data/input.txt")

    // 4. 切分数据
    val wordRDD: RDD[String] = lineRDD.flatMap(_.split(" "))

    val wordToOneRDD: RDD[(String, Int)] = wordRDD.map((_, 1))

    val wordToSumRDD: RDD[(String, Int)] = wordToOneRDD.reduceByKey(_ + _)

    wordToSumRDD.collect().foreach(println)

    // 5. 关闭连接
    sc.stop()
  }
}
