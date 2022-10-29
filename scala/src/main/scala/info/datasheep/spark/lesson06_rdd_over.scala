package info.datasheep.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * @description:
 * @author: shier
 * @date: 2022/10/29 16:22
 */
object lesson06_rdd_over {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local").setAppName("lesson06_rdd_over")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("ERROR")

    // 综合应用算子
    // TOPN 分组求TopN （二次排序）
    // 2019-6-2 39
    // 同月份中 温度最高的2天

    val file: RDD[String] = sc.textFile("./scala/data/tqdata.txt")
    // 2019-6-1	39
    val data: RDD[(Int, Int, Int, Int)] = file.map(_.split("\t")).map(arr => {
      val arrs: Array[String] = arr(0).split("-")
      // (year,month,day,wd)
      (arrs(0).toInt, arrs(1).toInt, arrs(2).toInt, arr(1).toInt)
    })

    implicit val sdfsdf = new Ordering[(Int, Int)] {
                    override def compare(x: (Int, Int), y: (Int, Int)) = y._2.compareTo(x._2)
                  }
    // 第一代
    // 用来groupByKey 容易OOM 且自己的算子实现了函数：去重、排序
    val grouped: RDD[((Int, Int), Iterable[(Int, Int)])] = data.map(t4 => ((t4._1, t4._2), (t4._3, t4._4))).groupByKey()
    val res01: RDD[((Int, Int), List[(Int, Int)])] = grouped.mapValues(arr => {
      val map = new mutable.HashMap[Int, Int]()
      arr.foreach(x => {
        if (map.get(x._1).getOrElse(0) < x._2) map.put(x._1, x._2)
      })
      map.toList.sorted(new Ordering[(Int, Int)] {
        override def compare(x: (Int, Int), y: (Int, Int)) = y._2.compareTo(x._2)
      })
    })
    println("----------第一代---------")
    res01.foreach(println)

    // 第二代
    // 用来groupByKey 容易OOM 取巧：spark rdd reduceByKey的取 max间接达到去重 让自己的算子变简单点
    val reduced: RDD[((Int, Int, Int), Int)] = data.map(t4 => ((t4._1, t4._2, t4._3), t4._4)).reduceByKey((x: Int, y: Int) => {
      if (y > x) y else x
    })
    val maped: RDD[((Int, Int), (Int, Int))] = reduced.map(t2 => ((t2._1._1, t2._1._2), (t2._1._3, t2._2)))
    val grouped2: RDD[((Int, Int), Iterable[(Int, Int)])] = maped.groupByKey()
    println("----------第二代---------")
    grouped2.mapValues(arr => arr.toList.sorted.take(2)).foreach(println)

    // 第三代
    // 用了groupByKey 容易OOM 取巧：用了reduceByKey去重，用了sortByKey排序
    // 注意：多级shuffle关注，后续触发的key一定得是前置rdd key的子集
    val sorted: RDD[(Int, Int, Int, Int)] = data.sortBy(t4 => (t4._1, t4._2, t4._4), false)
    val reduced2: RDD[((Int, Int, Int), Int)] = sorted.map(t4 => ((t4._1, t4._2, t4._3), t4._4)).reduceByKey((x: Int, y: Int) => if (y > x) y else x)
    val maped2: RDD[((Int, Int), (Int, Int))] = reduced2.map(t2 => ((t2._1._1, t2._1._2), (t2._1._3, t2._2)))
    val grouped3: RDD[((Int, Int), Iterable[(Int, Int)])] = maped2.groupByKey()
    println("----------第三代---------")
    grouped3.foreach(println)

    // 第四代
    // 用了 groupByKey 容易OOM 取巧：用了sortByKey排序 没有破坏多级shuffle的key的子集关系
    val sorted2: RDD[(Int, Int, Int, Int)] = data.sortBy(t4 => (t4._1, t4._2, t4._4), false)
    val grouped4: RDD[((Int, Int), Iterable[(Int, Int)])] = sorted2.map(t4 => ((t4._1, t4._2), (t4._3, t4._4))).groupByKey()
    println("----------第四代---------")
    grouped4.foreach(println)

    // 第五代
    // 分布式计算的核心思想：调优天下无敌：combineByKey
    // 分布式是并行的 离线批量计算有个特征就是后续步骤（stage）依赖前一步骤（stage）
    // 如果前一步骤（stage）能够加上正确的combineByKey
    // 自定义的combineBYKey的函数，是尽量压缩内存中的数据

    val kv: RDD[((Int, Int), (Int, Int))] = data.map(t4 => ((t4._1, t4._2), (t4._3, t4._4)))
    val res: RDD[((Int, Int), Array[(Int, Int)])] = kv.combineByKey(
      // 第一条记录怎么放：
      // (1, 38)
      (v1: (Int, Int)) => {
        Array(v1, (0, 0)) // ((1, 39), (0, 0))
      },
      // 第二条，以及后续的怎么放：
      // ((1, 39), (0, 0)) (1, 40)
      (oldv: Array[(Int, Int)], newv: (Int, Int)) => {
        // 去重 排序
        var flg = 0 // 0,1,2 新进来的元素特征： 日 a）相同 1）温度大 2）温度小 日 b）不同
        for (i <- 0 until (oldv.length)) {
          if (oldv(i)._1 == newv._1) {
            if (oldv(i)._2 < newv._2) {
              flg = 1
              oldv(i) = newv  // ((1, 40), (0, 0))
            }
          }
        }

        if (flg == 0) {
          oldv(oldv.length - 1) = newv
        }

        scala.util.Sorting.quickSort(oldv)
        oldv
      },
      (v1: Array[(Int, Int)], v2: Array[(Int, Int)]) => {
        // 关注去重
        val union: Array[(Int, Int)] = v1.union(v2)
        union.sorted
      }
    )
    println("----------第五代---------")
    res.map(x => (x._1, x._2.toList)).foreach(println)


    // map与mapvalues
    val data1: RDD[String] = sc.parallelize(List(
      "hello world",
      "hello spark",
      "hello world",
      "hello hadoop",
      "hello world",
      "hello msb",
      "hello world"
    ))
    val words: RDD[String] = data1.flatMap(_.split(" "))
    val kv1: RDD[(String, Int)] = words.map((_,1))
    val res1: RDD[(String, Int)] = kv1.reduceByKey(_+_)
    val res001: RDD[(String, Int)] = res1.map(x=>(x._1, x._2 * 10))
//    val res001: RDD[(String, Int)] = res1.mapValues(x=>x*10)
    val res002: RDD[(String, Iterable[Int])] = res001.groupByKey()
    res002.foreach(println)

    while (true) {

    }
  }
}
