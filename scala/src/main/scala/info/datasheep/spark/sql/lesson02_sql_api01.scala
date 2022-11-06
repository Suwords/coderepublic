package info.datasheep.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, Encoders, Row, SaveMode, SparkSession}

import scala.beans.BeanProperty

/**
 * @description:
 * @author: shier
 * @date: 2022/11/6 21:15
 */
class Person{
  @BeanProperty
  var name :String = ""
  @BeanProperty
  var age :Int = 0
}

object lesson02_sql_api01 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("sql_api")
    val session: SparkSession = SparkSession
      .builder()
      .config(conf)
      //      .enableHiveSupport()
      .getOrCreate()

    val sc: SparkContext = session.sparkContext
    sc.setLogLevel("ERROR")

//    import session.implicits._
//
//    val dataDF: DataFrame = List(
//      "hello world",
//      "hello world",
//      "hello spark",
//      "hello world",
//      "hello hadoop",
//      "hello world",
//      "hello hbase",
//      "hello world",
//      "hello flink",
//      "hello world"
//    ).toDF("line")
//
//    dataDF.createTempView("ooxx")
//
//    val df: DataFrame = session.sql("select * from ooxx")
//    df.show()
//    df.printSchema()
//
//    println("--------------")
//    session.sql("select word, count(*) from (select explode(split(line, ' ')) as word from ooxx) as tt group by tt.word ").show()
//    println("--------------")
//
//    // 面向API时，df相当于 from table
//    val res: DataFrame = dataDF.selectExpr("explode(split(line, ' ')) as word").groupBy("word").count()
//
//    // 以上两种方式，哪个更快？
//    // 为什么是第二种？ 省去了SQL解析的过程，其实差别不大
//    println("--------------")
//    res.write.mode(SaveMode.Append).parquet("./scala/out/ooxx")
//    println("--------------")
//
//    val frame: DataFrame = session.read.parquet("./scala/out/ooxx")
//    frame.show()
//    frame.printSchema()
//
//    /*
//    基于文件的输入：
//    session.read.parquet()
//    session.read.textFile()
//    session.read.json()
//    session.read.csv()
//    读取的任何格式的数据源都要转换成DF
//    res.write.parquet()
//    res.write.orc()
//    res.write.text()
//    */
//
//    // 数据 + 元数据 == df 就是一张表！！！
//
//    // 1. 数据 RDD[ROW]
//    // Spark 的 DataSet 既可以按collection，类似于RDD的方法操作，也可以按SQL领域语言定义的方式操作数据
//
//    /**
//     * 纯文本文件，不带自描述，String 在大数据中是不被待见的
//     * 必须转结构化，再参与计算
//     * 转换的过程可以由Spark完成
//     * hive数仓
//     * 数据接入：源数据  不删除，不破坏
//     * ETL 中间态
//     * 所有的计算发送在中间态
//     * 中间态：一切以后续计算成本为考量
//     * 文本格式类型？
//     * 分区、分桶
//     */
//    val ds: Dataset[String] = session.read.textFile("./scala/data/person.txt")
//    val person: Dataset[(String, Int)] = ds.map(
//      line => {
//        val strs: Array[String] = line.split(" ")
//        (strs(0), strs(1).toInt)
//      }
//    )(Encoders.tuple(Encoders.STRING, Encoders.scalaInt))
//
//    val cperson: DataFrame = person.toDF("name", "age")
//    cperson.show()
//    cperson.printSchema()
//
//    val rdd: RDD[String] = sc.textFile("./scala/data/person.txt")
//
//    // 第二种方式：bean类型的 rdd + javabean
//    // 数据+元数据 == df 就是一张表！！
//    val p = new Person()
//    // 1, mr,spark pipeline iter 一次内存飞过一条数据：：-》 这一条记录完成读取、计算、序列化
//    // 2, 分布式计算，计算逻辑由 Driver 序列化，发送给其他 JVM 的 Executor 中执行
//    val rddBean: RDD[Person] = rdd.map(_.split(" "))
//      .map(
//        arr => {
//          val p = new Person()
//          p.setName(arr(0))
//          p.setAge(arr(1).toInt)
//          p
//        }
//      )
//    val df01: DataFrame = session.createDataFrame(rddBean, classOf[Person])
//    df01.show()
//    df01.printSchema()


    // 第一点一版本，动态封装
    val userSchema: Array[String] = Array(
      "name string",
      "age int",
      "sex int"
    )
    // 1 row rdd

    def toDataType(vv: (String, Int)) = {
      userSchema(vv._2).split(" ")(1) match {
        case "string" => vv._1.toString
        case "int" => vv._1.toInt
      }
    }

    val rdd: RDD[String] = sc.textFile("./scala/data/person.txt")

    val rowRdd: RDD[Row] = rdd.map(_.split(" "))
      .map(x => x.zipWithIndex)
    // x == [(zhangsan,0) (18,1) (0,2)]
      .map(x => x.map(toDataType(_)))
      .map(x => Row.fromSeq(x)) // Row 代表很多列，每个列要标识出准确的类型

    // 2 structtype

    def getDataType(v:String) = {
      v match {
        case "string" => DataTypes.StringType
        case "int" => DataTypes.IntegerType
      }
    }

    val fields: Array[StructField] = userSchema.map(_.split(" ")).map(x => StructField.apply(x(0), getDataType(x(1))))
    val schema: StructType = StructType.apply(fields)

    val schema01: StructType = StructType.fromDDL("name string, age int, sex int")
    val df02: DataFrame = session.createDataFrame(rowRdd, schema01)
    df02.show()
    df02.printSchema()


    // 第一种方式： row类型的rdd + structType
    val rddRow: RDD[Row] = rdd.map(_.split(" ")).map(arr => Row.apply(arr(0), arr(1).toInt))

    // 2. 元数据 StructType
    val fields1: Array[StructField] = Array(
      StructField.apply("name", DataTypes.StringType, true),
      StructField.apply("age", DataTypes.IntegerType, true)
    )
    val schema02: StructType = StructType.apply(fields1)
    val dataFrame: DataFrame = session.createDataFrame(rddRow, schema02)
    dataFrame.show()
    dataFrame.printSchema()
    dataFrame.createTempView("person")
    session.sql("select * from person").show()

    // metastore catalog
    session.sql("create table ooxx(name string, age int)")
    session.catalog.listTables().show()
  }

}
