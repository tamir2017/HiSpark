package cn.learn.spark

import org.apache.spark.{SparkConf, SparkContext}

object ForeachDemo {
  def main(args: Array[String]): Unit = {
    // 设置本地模式，local的默认启动进程数为 1， 若启动多个进程（2），则 local[2]
    val conf = new SparkConf().setAppName("ForeachDemo").setMaster("local")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 3)
    rdd1.foreach(println(_))
    sc.stop()
  }
}
