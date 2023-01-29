package info.datasheep.spark.sql

import org.apache.spark.sql.catalog.{Database, Table}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession, catalog}

/**
 * @description:
 * @author: shier
 * @date: 2022/11/6 21:01
 */
object lesson01_sql_basic {
  def main(args: Array[String]): Unit = {
    // sql 字符串 -》 dataset 对rdd的一个包装（优化器） -》 只有RDD才能触发DAGScheduler

    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("sql_basic")
    val session: SparkSession = SparkSession
      .builder()
      .config(conf)
      //.enableHiveSupport() // 开启这个选项时， spark on hive 才支持DDL，没开启，spark只有Catalog
      .getOrCreate()

    val sc: SparkContext = session.sparkContext
    sc.setLogLevel("ERROR")

    // 以session为主进行操作
    // DataFrame DataSet[Row]

    // 以SQL为中心
    val databases: Dataset[Database] = session.catalog.listDatabases()
    databases.show()

    val tables: Dataset[Table] = session.catalog.listTables()
    tables.show()

    val functions: Dataset[catalog.Function] = session.catalog.listFunctions()
    functions.show(999, true)

    println("--------------------")

    val df: DataFrame = session.read.json("./scala/data/json")
    df.show()
    df.printSchema()

    df.createTempView("ooxx") // 这个过程中df通过session向Catalog中注册表名

//    val frame: DataFrame = session.sql("select name from ooxx")
//    frame.show()
//    println("-----------")
//    session.catalog.listTables().show()
//
    import scala.io.StdIn._

    while (true) {
      val sql: String = readLine("input your sql: ")
      session.sql(sql).show()
    }
  }
}
