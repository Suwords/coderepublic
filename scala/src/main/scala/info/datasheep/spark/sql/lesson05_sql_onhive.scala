package info.datasheep.spark.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @description:
 * @author: shier
 * @date: 2022/11/6 22:31
 */
object lesson05_sql_onhive {
  def main(args: Array[String]): Unit = {
    val ss: SparkSession = SparkSession
      .builder()
      .appName("test on hive")
      .master("local")
      .config("hive.metastore.uris", "thrift://cdh01:9083")
      .enableHiveSupport()
      .getOrCreate()

    val sc: SparkContext = ss.sparkContext
    sc.setLogLevel("ERROR")

    import ss.implicits._

    val df: DataFrame = List(
      "zhangsan",
      "lisi"
    ).toDF("name")
    df.createTempView("ooxx")

    ss.sql("create table xxoo(id int)") // ddl

    ss.sql("insert into xxoo values (1),(2)") // DML 数据是通过Spark自己和hdfs进行访问
    df.write.saveAsTable("oxox")

    ss.catalog.listTables().show()

    // 如果没有hive的时候，表最开始一定是 DataSet、DataFrame
    val df01: DataFrame = ss.sql("show tables")
    df01.show()
  }
}
