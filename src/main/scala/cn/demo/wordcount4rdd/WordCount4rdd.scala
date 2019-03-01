package cn.demo.wordcount4rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * WordCount 过程会产生 6 个 RDD,
  * 具体过程分析如下：
  */
object WordCount {
  def main(args: Array[String]) {
    //非常重要，是通向Spark集群的入口
    val conf = new SparkConf().setAppName("WC")

    val sc = new SparkContext(conf)

    //textFile会产生两个RDD： HadoopRDD  -> MapPartitinsRDD
    sc.textFile(args(0))
      // 产生一个RDD ：MapPartitinsRDD
      .flatMap(_.split(" "))
      //产生一个RDD MapPartitionsRDD
      .map((_, 1))
      //产生一个RDD ShuffledRDD
      .reduceByKey(_+_)
      //产生一个RDD: mapPartitions
      .saveAsTextFile(args(1))
    sc.stop()
  }
}
