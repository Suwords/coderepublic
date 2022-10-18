package info.datasheep.lesson

import java.util

/**
 * @description:
 * @author: shier
 * @date: 2022/10/18 22:34
 */
object Lesson08_implicit {
  def main(args: Array[String]): Unit = {
    val linkedList = new util.LinkedList[Int]()
    linkedList.add(1)
    linkedList.add(2)
    linkedList.add(3)

    val arrayList = new util.ArrayList[Int]()
    arrayList.add(1)
    arrayList.add(2)
    arrayList.add(3)

    // 隐式转换： 隐式转换方法
//    implicit def sdfsdf[T](list: util.LinkedList[T]) = {
//      val iter: util.Iterator[T] = list.iterator()
//      new XXX(iter)
//    }
//
//    implicit def sdfjhksdf[T](list: util.ArrayList[T]) = {
//      val iter: util.Iterator[T] = list.iterator()
//      new XXX(iter)
//    }
//
//    linkedList.foreach(println)
//    arrayList.foreach(println)

    // spark RDD N方法 scala
    // 隐式转换类
    implicit class KKK[T](list: util.LinkedList[T]){
      def foreach(f: (T) => Unit): Unit = {
        val iter: util.Iterator[T] = list.iterator()
        while (iter.hasNext) f(iter.next())
      }
    }
    linkedList.foreach(println) // 必须先承认：list中没有foreach方法，在java中这么写肯定报错

    // 这些代码最终交给了scala的编译器！！
    // 1. scala编译器发现 linkedList.foreach(println) 有bug
    // 2. 去寻找有没有implicit 定义的方法，且方法的参数真好是list的类型
    // 3. 编译期：完成曾经人类的操作：
    // val xx = new XXX(list)
    // xx.foreach(println)
    // 即编译器帮你把代码改写了！！！

    // 隐式转换：变量
    implicit val sdfs: String = "wewe"
    implicit val sss: Int = 88
    def ooxx(age: Int)(implicit name: String): Unit = {
      println(s"$name $age")
    }
    ooxx(66)
    ooxx(66)("jdjhka")
  }
}

class XXX[T](list: util.Iterator[T]) {
  def foreach(f: (T) => Unit): Unit = {
    while (list.hasNext) f(list.next())
  }
}
