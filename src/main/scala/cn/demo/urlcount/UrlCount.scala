package cn.demo.urlcount

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 取出学科点击前三的
  * 缺点：排序算法使用java的函数，可能数据倾斜问题
  * 改进：使用 RDD 排序方法，详见 AdvUrlCount
  */
object UrlCount {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("UrlCount").setMaster("local[2]")
    val sc = new SparkContext(conf)

    //rdd1将数据切分，元组中放的是（URL， 1）
    val rdd1 = sc.textFile("c://itcast.log").map(line => {
      val f = line.split("\t")
      (f(1), 1)
    })
    val rdd2 = rdd1.reduceByKey(_+_)

    val rdd3 = rdd2.map(t => {
      val url = t._1
      val host = new URL(url).getHost
      (host, url, t._2)
    })

    // 排序算法使用java的函数，可能数据倾斜问题
    val rdd4 = rdd3.groupBy(_._1).mapValues(it => {
      it.toList.sortBy(_._3).reverse.take(3)
    })

    println(rdd4.collect().toBuffer)
    sc.stop()

  }
}
