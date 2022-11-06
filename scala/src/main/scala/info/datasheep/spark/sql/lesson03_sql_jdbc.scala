package info.datasheep.spark.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

import java.util.Properties

/**
 * @description:
 * @author: shier
 * @date: 2022/11/6 22:14
 */
object lesson03_sql_jdbc {
  def main(args: Array[String]): Unit = {
    val ss: SparkSession = SparkSession
      .builder()
      .appName("ssss")
      .master("local")
      .config("spark.sql.shuffle.partitions", "1") // 默认会有200并行度的参数， 是shuffle
      .getOrCreate()

    val sc: SparkContext = ss.sparkContext
    sc.setLogLevel("ERROR")

    val pro = new Properties()
    pro.put("url", "jdbc:mysql://localhost/spark")
    pro.put("user", "root")
    pro.put("password", "hadoop")
    pro.put("driver", "com.mysql.jdbc.Driver")

    val usersDF: DataFrame = ss.read.jdbc(pro.get("url").toString, "users", pro)
    val scoreDF: DataFrame = ss.read.jdbc(pro.get("url").toString, "scores", pro)
    usersDF.createTempView("userstab")
    scoreDF.createTempView("scoretab")

    val resDF: DataFrame = ss.sql("select userstab.id, userstab.name, userstab.age, scoretab.score from userstab join scoretab on userstab.id = scoretab.id ")
    resDF.show()
    resDF.printSchema()

    println(resDF.rdd.partitions.length)
    val resDF01: Dataset[Row] = resDF.coalesce(1)
    println(resDF01.rdd.partitions.length)

    resDF.write.jdbc(pro.get("url").toString, "bbb", pro)
  }
}
