package cn.demo.urlcount

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 根据指定的学科, 取出点击量前三的
  * 改进：使用 RDD 排序方法，详见 AdvUrlCount
  */
object AdvUrlCount {

  def main(args: Array[String]) {

    //从数据库中加载规则
    val arr = Array("java.itcast.cn", "php.itcast.cn", "net.itcast.cn")

    val conf = new SparkConf().setAppName("AdvUrlCount").setMaster("local[2]")
    val sc = new SparkContext(conf)
    //rdd1将数据切分，元组中放的是（URL， 1）
    val rdd1 = sc.textFile("c://itcast.log").map(line => {
      val f = line.split("\t")
      (f(1), 1)
    })
    val rdd2 = rdd1.reduceByKey(_ + _)

    val rdd3 = rdd2.map(t => {
      val url = t._1
      val host = new URL(url).getHost
      (host, url, t._2)
    })

    //println(rdd3.collect().toBuffer)

//    val rddjava = rdd3.filter(_._1 == "java.itcast.cn")
//    val sortdjava = rddjava.sortBy(_._3, false).take(3)
//    val rddphp = rdd3.filter(_._1 == "php.itcast.cn")

    for (ins <- arr) {
      val rdd = rdd3.filter(_._1 == ins)
      //排序 使用 RDD 排序方法
      val result= rdd.sortBy(_._3, false).take(3)
      //通过JDBC向数据库中存储数据
      //id，学院，URL，次数， 访问日期
      println(result.toBuffer)
    }

    //println(sortdjava.toBuffer)
    sc.stop()

  }
}
