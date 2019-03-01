package cn.demo.sql

import java.util.Properties

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object WriteDataToMysql {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WriteDataToMysql").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    //通过并行化创建RDD
    val urlRDD = sc.parallelize(Array("http://www.baidu.com 2", "http://www.alibaba.com 3")).map(_.split(" "))
    //通过StructType直接指定每个字段的schema
    val schema = StructType(
      List(
        StructField("url", StringType, true),
        StructField("content", StringType, true)
      )
    )
    //将RDD映射到rowRDD
    val rowRDD = urlRDD.map(p => Row(p(0).trim, p(1).trim))
    //将schema信息应用到rowRDD上
    val personDataFrame = sqlContext.createDataFrame(rowRDD, schema)
    //创建Properties存储数据库相关属性
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "123")
    //将数据追加到数据库
    personDataFrame.write.mode("append").jdbc("jdbc:mysql://192.168.2.181:3306/urldb", "urldb.url_rule", prop)
    //停止SparkContext
    sc.stop()
  }
}
