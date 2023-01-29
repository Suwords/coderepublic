package cn.coderepublic.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description: 测试Spark的分区规则
 * @author: shier
 * @date: 2023/1/29 20:33
 */
object FilePartition {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local").setAppName("FilePartition")
    val sc = new SparkContext(sparkConf)

    // 默认填写的最小分区数2和环境的核数取最小的值 一般为2
    // math.min(defaultParallelism,2)
    // val lineRDD: RDD[String] = sc.textFile("spark-warehouse/data/input.txt", 3)
    val lineRDD: RDD[String] = sc.textFile("spark-warehouse/data/input.txt")
    println(lineRDD.getNumPartitions)
    // 具体的分区个数需要经过公式计算
    // 首先获取文件的总长度 totalSize
    // 计算平均长度 goalSize = totalSize / numSplits
    // 获取块大小 128M
    // 计算切片大小 splitSize = Math.max(minSize,Math.min(goalSize,blockSize));
    // 最后使用 splitSize 按照1.1倍原则切分整个文件 得到几个分区就是几个分区
    // 实际开发中 只需要看文件总大小 / 填写的分区数，和块大小比较，谁小拿谁进行切分
    lineRDD.saveAsTextFile("output")

    // 数据会分配到哪个分区
    // 如果切分的位置位于一行的中间，会在当前分区读完一整行数据

    sc.stop()
  }
}
