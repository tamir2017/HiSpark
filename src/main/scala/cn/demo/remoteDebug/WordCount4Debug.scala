package cn.demo.remoteDebug

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 远程 debug 项目
  *
  */
object WordCount4Debug {
  def main(args: Array[String]) {
    //非常重要，是通向Spark集群的入口
    val conf = new SparkConf().setAppName("WordCount4Debug")
      .setJars(Array("F:\\java\\workspace\\hiSpark\\target\\hiSpark-1.0.jar"))
      .setMaster("spark://192.168.2.181:7077")

    val sc = new SparkContext(conf)

    sc.textFile(args(0)).cache()
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_+_)
      .saveAsTextFile(args(1))
    sc.stop()
  }
}
