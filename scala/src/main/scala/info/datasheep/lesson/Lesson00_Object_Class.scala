package info.datasheep.lesson

/**
 * @description:
 * @author: shier
 * @date: 2022/10/15 16:54
 */
object Hello {

  val a = 1
  var b = "bbxx"

  val c: Int = 2
  val d: String = "String"

  def main(args: Array[String]): Unit = {
    println("Hello,World")

    val person = new Person("zhang", "san")
    println(person.firstName)
    person.lastName = "si"
    person.printFullName()
  }
}

class ooxx(sex: String) {
  var name = "class:zhangsan"

  def this(xname: Int){
    // 必须调用默认构造
    this("abc")
  }

  var a: Int = 3

  println(s"ooxx...up$a...")

//  def printMsg(): Unit = {
//    println(s"sex: ${ooxx.name}")
//  }

  println(s"ooxx...up${a + 4}")
}

class Person(var firstName: String, var lastName: String) {
  def printFullName() = println(s"$firstName $lastName")
}