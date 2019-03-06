package cn.demo.Streaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingWordCount {
  def main(args: Array[String]): Unit = {
    LoggerLevels.setStreamingLogLevels()
    val conf = new SparkConf().setAppName("StreamingWordCount").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(5))

    val ds = ssc.socketTextStream("192.168.2.181", 8888)
    val res = ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    res.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
