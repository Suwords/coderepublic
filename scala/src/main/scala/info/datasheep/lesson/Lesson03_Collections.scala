package info.datasheep.lesson

import java.util
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @description:
 * @author: shier
 * @date: 2022/10/18 20:26
 */
object Lesson03_Collections {

  def main(args: Array[String]): Unit = {

    // 如果使用java
    val listJava = new util.LinkedList[String]()
    listJava.add("hello")

    // scala有自己的collections
    // 1. 数组
    // Java中泛型是<> scala中是[]，所以数组用 (n)
    // val 约等于 final 不可变描述的是val指定的引用的值（值：字面值，地址）
    val arr01 = Array[Int](1, 2, 3, 4)
    arr01(1) = 99
    println(arr01(0))
    for (elem <- arr01) {
      println(elem)
    }
    // 遍历元素，需要函数接收元素
    arr01.foreach(println)

    println("============ List ===========")

    // 2. 链表
    // scala中collections中有2个包：immutable, mutable 默认的是不可变的 immutable
    val list01 = List(1, 2, 3, 4, 5, 4, 3, 2, 1)
    for (elem <- list01) {
      println(elem)
    }

    list01.foreach(println)

    val list02 = new ListBuffer[Int]()
    list02.+=(33)
    list02.+=(34)
    list02.+=(35)

    list02.foreach(println)

    println("==========Set=========")

    val set01: Set[Int] = Set(1, 2, 3, 4, 2, 1)
    for(elem <- set01) {
      println(elem)
    }
    set01.foreach(println)

    import scala.collection.mutable.Set
    val set02: mutable.Set[Int] = Set(11, 12, 13, 14, 14, 13)
    set02.add(88)

    set02.foreach(println)

    val set03: Predef.Set[Int] = scala.collection.immutable.Set(33, 22, 11, 22, 33)

    println("========== tuple ==========")

    val t2 = (11, "sdada") // 2元素的Tuple2，在scala中描绘的是K，V
    val t3 = Tuple3(22, "dasada", 's')
    val t4: (Int, Int, Int, Int) = (1, 2, 3, 4)
    val t22: ((Int, Int) => Int, Int) = ((_ + _), 2)

    println(t2._1)
    println(t4._3)
    println(t22._1(8,2))

    val iterator: Iterator[Any] = t22.productIterator
    while (iterator.hasNext) {
      println(iterator.next())
    }

    println("=========map==========")
    val map01: Map[String, Int] = Map(("a", 33), "b"->22, ("c",3434),("a",3333))
    val keys: Iterable[String] = map01.keys

    // option: none some
    println(map01.get("a").get)

    println(map01.get("a").getOrElse("hello world"))
    println(map01.get("w").getOrElse("hello world"))

    for (elem <- keys) {
      println(s"key: $elem value: ${map01.get(elem).get}")
    }

    val map02: mutable.Map[String, Int] = scala.collection.mutable.Map(("a", 11), ("b", 22))
    map02.put("c", 22)

    println("============艺术==========")
    val list = List(1, 2, 3, 4, 5, 6)
    list.foreach(println)
    val listMap: List[Int] = list.map((x: Int) => x + 10)
    listMap.foreach(println)
    val listMap02: List[Int] = list.map(_ * 2)
    list.foreach(println)
    listMap02.foreach(println)

    println("=========艺术-升华==========")

    val listStr = List (
      "hello world",
      "hello msb",
      "good idea"
    )

    val flatMap = listStr.flatMap(_.split(" "))
    flatMap.foreach(println)
    val mapList = flatMap.map((_, 1))
    mapList.foreach(println)
    // 以上代码，内存扩大了N倍，每一步计算内存都留有对象数据，有没有什么现成的技术解决数据计算中间状态占用内存这一问题？
    // iterator！！！
    println("========艺术-再-升华==========")

    // 基于迭代器的源码分析
    val iter: Iterator[String] = listStr.iterator // 什么是迭代器，为什么会有迭代器模式？ 迭代器里不存数据！！！

    val iterFlatMap: Iterator[String] = iter.flatMap(_.split(" "))

    val iterMapList: Iterator[(String, Int)] = iterFlatMap.map((_, 1))

    while (iterMapList.hasNext) {
      val tuple: (String, Int) = iterMapList.next()
      println(tuple)
    }

    // 1. listStr真正的数据集，有数据的
    // 2. iter.flatMap 没有发生计算，返回了一个新的迭代器

  }

}
