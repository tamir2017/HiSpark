package cn.demo.sql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object ReadDataFromMysql {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("DataFromMysql").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val jdbcDF = sqlContext.read.format("jdbc")
      .options(Map("url" -> "jdbc:mysql://192.168.2.181:3306/urldb",
        "driver" -> "com.mysql.jdbc.Driver",
        "dbtable" -> "url_rule",
        "user" -> "root",
        "password" -> "123")).load()
    jdbcDF.registerTempTable("person")
    sqlContext.sql("select * from person").show()
    sc.stop()
  }
}
