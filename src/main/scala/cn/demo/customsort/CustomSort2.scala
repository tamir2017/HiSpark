package cn.demo.customsort

import org.apache.spark.{SparkConf, SparkContext}

/**
  *   第二种，通过隐式转换完成排序
  */
//sort =>规则 先按 faveValue，比较年龄
//name,faveValue,age


// 门面类，具体的比较规则，
// 注意必须是 object 开头
object OrderContext {
  implicit val girl2Ordering  = new Ordering[Girl2] {
    override def compare(x: Girl2, y: Girl2): Int = {
      if(x.faceValue > y.faceValue) 1
      else if (x.faceValue == y.faceValue) {
        if(x.age > y.age) -1 else 1
      } else -1
    }
  }
}


/**
  *   第二种，通过隐式转换完成排序
  */
//sort =>规则 先按faveValue，比较年龄
//name,faveValue,age


object CustomSort2 {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("CustomSort2").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(("yuihatano", 90, 28, 1), ("angelababy", 90, 27, 2),("JuJingYi", 95, 22, 3)))
    // 导入门面类，即具体的排序规则
    import OrderContext._
    val rdd2 = rdd1.sortBy(x => Girl2(x._2, x._3), false)
    println(rdd2.collect().toBuffer)
    sc.stop()
  }

}

/**
  * 第二种，通过隐式转换完成排序
  * @param faceValue
  * @param age
  */
case class Girl2(faceValue: Int, age: Int) extends Serializable