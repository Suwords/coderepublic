package info.datasheep.lesson

/**
 * @description:
 * @author: shier
 * @date: 2022/10/18 22:24
 */
object Lesson06_match {
  def main(args: Array[String]): Unit = {
    val tup = (1.0, 88, "abc", false, 44)
    val iter: Iterator[Any] = tup.productIterator
    val res: Iterator[Unit] = iter.map(
      (x) => {
        x match {
          case 1 => println(s"$x...is 1")
          case 88 => println(s"$x...is 88")
          case false => println(s"$x...is false")
          case w: Int if w > 50 => println(s"$w...is > 50")
          case _ => println("!!!!")
        }
      }
    )
    while (res.hasNext){
      println(res.next())
    }
  }
}
