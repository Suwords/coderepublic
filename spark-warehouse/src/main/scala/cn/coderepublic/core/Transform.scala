package cn.coderepublic.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/**
 * @description: Transform算子
 * @author: shier
 * @date: 2023/1/29 21:14
 */
class Transform {

  private val sparkConf: SparkConf = new SparkConf().setMaster("local").setAppName("transform")
  private val sc = new SparkContext(sparkConf)
  /**
   * 功能描述：创建一个1-4数组的RDD，两个分区，将所有元素*2形成新的RDD
   * @param: []
   * @return: void
   * @author: shier
   * @date: 2023/1/29
  */
  @Test
  def map() = {
    val list = List(1, 2, 3, 4)
    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    val mapRDD: RDD[Int] = rdd.map(_ * 2)

    mapRDD.collect().foreach(println)
  }

  /**
   * 功能描述：创建一个1-4数组的RDD，两个分区，将所有元素*2形成新的RDD
   * @param: []
   * @return: void
   * @author: shier
   * @date: 2023/1/29
  */
  @Test
  def mapPartitions(): Unit ={
    val rdd: RDD[Int] = sc.makeRDD(1 to 4, 2)
    val valueRDD: RDD[Int] = rdd.mapPartitions(datas => datas.map(_ * 2))
    valueRDD.collect().foreach(println)
  }

  /**
   * 功能描述：创建一个RDD，使每个元素跟所在分区号形成一个元组，组成新的RDD
   * @param: []
   * @return: void
   * @author: shier
   * @date: 2023/1/29
  */
  @Test
  def mapPartitionsWithIndex() = {
    val rdd: RDD[Int] = sc.makeRDD(1 to 10, 3)

    val valueRDD: RDD[(Int, Int)] = rdd.mapPartitionsWithIndex((index, items) => {
      items.map((index, _))
    })

    valueRDD.collect().foreach(println)
  }

  /**
   * 功能描述：创建一个集合，集合里面存储的还是子集合，把所有子集合中数据取出放入到一个大的集合中
   * @param: []
   * @return: void
   * @author: shier
   * @date: 2023/1/29
  */
  @Test
  def flatMap() = {
    val rdd: RDD[List[Int]] = sc.makeRDD(List(List(1, 2), List(3, 4), List(5, 6, 7, 8)), 2)

    rdd.flatMap(data => data).collect().foreach(println)
  }

  /**
   * 功能描述：创建一个 RDD，按照元素模以2的值进行分组
   * @param: []
   * @return: void
   * @author: shier
   * @date: 2023/1/29
  */
  @Test
  def groupBy() = {
    val rdd: RDD[Int] = sc.makeRDD(1 to 4, 2)

    rdd.groupBy(x => x % 2).collect().foreach(println)
  }

  /**
   * 功能描述：创建一个RDD，过滤出对2取余等于0的数据
   * @param: []
   * @return: void
   * @author: shier
   * @date: 2023/1/29
  */
  @Test
  def filter() = {
    val rdd: RDD[Int] = sc.makeRDD(1 to 4, 2)

    rdd.filter(_ % 2 == 0).collect().foreach(println)
  }
}
