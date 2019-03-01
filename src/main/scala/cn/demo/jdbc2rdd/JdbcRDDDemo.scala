package cn.demo.jdbc2rdd

import java.sql.DriverManager

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 从本地 MYSQL数据库中读取数据到 RDD 中。
  */
object JdbcRDDDemo {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("JdbcRDDDemo").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val connection = () => {
      Class.forName("com.mysql.jdbc.Driver").newInstance()
      DriverManager.getConnection("jdbc:mysql://localhost:3306/urldb", "root", "123")
    }
    val jdbcRDD = new JdbcRDD(
      sc,
      connection,
      "SELECT * FROM location_info where id >= ? AND id <= ?",
      6,  // 查询开始 id 值
      10, // 查询结束 id 值
      2,  //  查询并发进程数
      r => {
        val id = r.getInt(1)
        val code = r.getString(2)
        val counts = r.getString(3)
        val date = r.getString(4)
        (id, code, counts, date)
      }
    )
    val jrdd = jdbcRDD.collect()
    println(jdbcRDD.collect().toBuffer)
    sc.stop()
  }
}
