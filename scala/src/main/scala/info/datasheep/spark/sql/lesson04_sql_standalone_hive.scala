package info.datasheep.spark.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

/**
 * @description:
 * @author: shier
 * @date: 2022/11/6 22:23
 */
object lesson04_sql_standalone_hive {
  def main(args: Array[String]): Unit = {
    val ss: SparkSession = SparkSession
      .builder()
      .master("local")
      .appName("ssds")
      .config("spark.sql.shuffle.partitions", 1)
      .config("spark.sql.warehouse.dir", "./scala/spark/warehouse")
      .enableHiveSupport() // 开启hive支持 自己会启动hive的MetaStore
      .getOrCreate()

    val sc: SparkContext = ss.sparkContext
    sc.setLogLevel("ERROR")

    import ss.sql

    /**
     * 一定要记住有数据库的概念！！！！
     * use default
     * mysql 是个软件
     * 一个mysql可以创建很多的库 database 隔离的
     * 公司只装一个mysql，不同的项目组，自己用自己的库 database
     *
     *
     * spark hive 一样的
     */

    ss.sql("create table xxx(name string, age int)")
    ss.sql("insert into xxx values('zhangsan', 18),('lisi', 22)")

    sql("create database ddbb")
    sql("create table ddbb.xxxx(name string, age int)")
    sql("create table xxxx(name string, age int)")

    ss.catalog.listTables().show() // 作用在Current库

    sql("use ddbb")
    ss.catalog.listTables().show() // 作用在 ddbb 库
    sql("create table table02(name string)") // 作用在 ddbb库
    ss.catalog.listTables().show() // 作用在 ddbb 库

  }

}
