package cn.demo.customsort

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 第一种方式   extends Ordered[T]  实现 compare 方法
  * //sort =>规则 先按faveValue，比较 age
  *
  */

object CustomSort1 {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("CustomSort1").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(("yuihatano", 90, 28, 1), ("angelababy", 90, 27, 2),("JuJingYi", 95, 22, 3)))
    val rdd2 = rdd1.sortBy(x => Girl(x._2, x._3), false)
    println(rdd2.collect().toBuffer)
    sc.stop()
  }

}

// 此处使用 case class 目的是不用使用 new 关键字 来实例化对象，更方便。
case class Girl(val faceValue: Int, val age: Int) extends Ordered[Girl] with Serializable {
  override def compare(that: Girl): Int = {
    if(this.faceValue == that.faceValue) {
      that.age - this.age
    } else {
      this.faceValue -that.faceValue
    }
  }
}


