package info.datasheep.lesson

/**
 * @description:
 * @author: shier
 * @date: 2022/10/18 22:28
 */
object Lesson07_PartialFunction {
  def main(args: Array[String]): Unit = {
    def xxx: PartialFunction[Any, String] = {
      case "hello" => "val is hello"
      case x: Int => s"$x...is int"
      case _ => "none"
    }

    println(xxx(44))
    println(xxx("hello"))
    println(xxx("hi"))
  }

}
