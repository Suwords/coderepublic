package info.datasheep.lesson

import java.util.Date
import scala.collection.mutable

/**
 * @description:
 * 语法 =》 编译器 =》 字节码 《= jvm规则
 * 编译器衔接人和机器
 *
 * 编译型 C 《 贼快
 * 解释型 python 《 贼慢
 *
 * Java 其实不值钱，最值钱的是JVM
 * Java：解释型，编译过程，类型 比 python快
 * JVM：为什么值钱，是C写的，【字节码（二进制） =》 JVM（堆/堆外（二进制））《 kernel(mmap,sendfile)】 更快！！
 * @author: shier
 * @date: 2022/10/17 22:07
 */
object Lesson02_Functions {

  def ooxx(): Unit = {
    println("hello object")
  }

  def main(args: Array[String]): Unit = {
    // 方法 函数
    println("============1. basic =============")

    // 返回值 参数 函数体
    def fun01(): Unit = {
      println("hello world")
    }

    fun01()

    var x = 3
    var y = fun01()
    println(y)

    def fun02() = {
      new mutable.LinkedList[String]()
    }

    // 参数：必须给出类型，val
    def fun03(a: Int) = {
      println(a)
    }

    fun03(3)

    println("==========2. 递归函数 ==========")

    // 递归先写触底，触发什么报错
    def fun04(num: Int): Int = {
      if (num == 1) {
        num
      } else {
        num * fun04(num - 1)
      }
    }

    val i = fun04(4)
    println(i)

    println("==========3. 默认值函数 =========")

    def fun05(a: Int = 8, b: String = "abc") = {
      println(s"$a...$b")
    }

    fun05(22)
    fun05(b = "oooxxx")

    println("=========4. 匿名函数 ===========")

    // 函数是第一类值
    // 函数：
    // 1. 签名： (Int, Int) => Int ：（参数类型列表） =》 返回值类型
    // 2. 匿名函数：(a:Int,b:Int) => {a+b} ：（参数实现列表） =》 函数体
    var xx: Int = 3

    var yy: (Int, Int) => Int = (a: Int, b: Int) => {
      a + b
    }

    val w = yy(3, 4)
    println(w)

    println("========5. 嵌套函数 ============")

    def fun06(a: String): Unit = {
      def fun05(): Unit = {
        println(a)
      }

      fun05()
    }

    fun06("hello")

    println("==========6. 偏应用函数 ===========")

    def fun07(date: Date, tp: String, msg: String) = {
      println(s"$date\t$tp\t$msg")
    }

    fun07(new Date(), "info", "ok")

    var info = fun07(_: Date, "info", _: String)
    var error = fun07(_: Date, "error", _: String)
    info(new Date, "ok")
    error(new Date, "error")

    println("==========7. 可变参数 =========")

    def fun08(a: Int*): Unit = {
      for (e <- a) {
        println(e)
      }
      a.foreach(println)
    }

    fun08(2)
    fun08(1, 2, 3, 4)

    println("==========8. 高阶函数 ===========")

    // 函数作为参数，函数作为返回值
    // 函数作为参数
    def computer(a: Int, b: Int, f: (Int, Int) => Int): Unit = {
      val res = f(a, b)
      println(res)
    }

    computer(3, 8, (x: Int, y: Int) => {
      x + y
    })
    computer(3, 8, (x: Int, y: Int) => {
      x * y
    })
    computer(3, 8, _ * _)

    // 函数作为返回值
    def factory(i: String): (Int, Int) => Int = {
      def plus(x: Int, y: Int): Int = {
        x + y
      }

      if (i.equals("+")) {
        plus
      } else {
        (x: Int, y: Int) => {
          x - y
        }
      }
    }

    computer(3, 8, factory("-"))

    println("=========9. 柯里化 ==========")

    def fun09(a: Int)(b: Int)(c: String): Unit = {
      println(s"$a\t$b\t$c")
    }

    fun09(3)(8)("sdsad")

    def fun10(a: Int*)(b: String*): Unit = {
      a.foreach(println)
      b.foreach(println)
    }

    fun10(1, 2, 3)("sdfs", "sss")

    println("======*. 方法 =========")

    // 方法不想执行，赋值给一个引用，方法名+空格+下划线
    val funa = ooxx
    println(funa)
    val func = ooxx _
    func()

  }
}
