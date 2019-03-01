package cn.demo.sql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 功能： 从 hdfs 中读取数据， 数据处理方式 ： rdd -> df ->sql
  */
object SQLDemo {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SQLDemo").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    System.setProperty("user.name","hadoop")
    val personRdd = sc.textFile("hdfs://hbase1:9000/person.txt").map(line =>{
      val fields = line.split(",")
      Person(fields(0).toLong, fields(1), fields(2).toInt)   // 将 RDD和 case class关联
    })
    //导入隐式转换，如果不到人无法将RDD转换成DataFrame
    //将RDD转换成DataFrame
    import sqlContext.implicits._
    val personDf = personRdd.toDF

    personDf.registerTempTable("person")
    sqlContext.sql("select * from person where age >= 10 order by age desc limit 2").show()

    sc.stop()
  }
}
//case class一定要放到外面
case class Person(id: Long, name: String, age: Int)