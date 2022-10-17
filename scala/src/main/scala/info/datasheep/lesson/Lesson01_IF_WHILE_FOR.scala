package info.datasheep.lesson

import scala.collection.parallel.immutable

/**
 * @description:
 * @author: shier
 * @date: 2022/10/17 21:57
 */
object Lesson01_IF_WHILE_FOR {
  def main(args: Array[String]): Unit = {
    // if while for
    var a = 0
    if (a < 0) {
      println(s"$a<0")
    } else {
      println(s"$a>=0")
    }

    var aa = 0
    while (aa < 10) {
      println(aa)
      aa += 1
    }

    println("======================")

    val seqs = 1 until 10
    println(seqs)

    for (i <- seqs if i % 2 == 0) {
      println(i)
    }

    println("=====================")

    var num = 0
    for (i <- 1 to 9; j <- 1 to 9 if (j <= i)){
      num += 1
      if (j <= i) print(s"$i * $j = ${i * j}\t")
      if (j == i) println()
    }
    println(num)

    val seqss = for (i <- 1 to 10) yield {
      var x = 8
      i + x
    }

    for (i <- seqss) {
      println(i)
    }
  }
}
