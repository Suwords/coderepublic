package info.datasheep.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: shier
 * @date: 2022/10/22 10:23
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)

    val dataRDD: RDD[String] = sparkContext.textFile("./scala/data/wordcount.txt")

    //dataRDD.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).foreach(println)

    val wordsRDD: RDD[String] = dataRDD.flatMap(_.split(" "))

    val wordRDD: RDD[(String, Int)] = wordsRDD.map((_, 1))

    val result: RDD[(String, Int)] = wordRDD.reduceByKey(_ + _)

    result.foreach(println)

    result.map((x) => {(x._2, 1)}).reduceByKey(_ + _).foreach(println)

    //Thread.sleep(Integer.MAX_VALUE)
  }

}
