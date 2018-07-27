package cn.itcast.spark.day2

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by root on 2016/5/16.
  */
object ForeachDemo {

  def main(args: Array[String]) {
    /*local[n]中的n代表线程数*/
    val conf = new SparkConf().setAppName("ForeachDemo").setMaster("local")
    val sc = new SparkContext(conf)

    val rdd1 = sc.parallelize(List(1,2,3,4,5,6,7),2)
    rdd1.foreach(println(_))

    //RDD[(K,V)]
    //
    //

    sc.stop()

  }
}
