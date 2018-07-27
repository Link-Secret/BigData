package cn.itcast.spark.day2own

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/26 15:01
  * \* Description: 
  * \*/
object AdvUrlCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UrlCount").setMaster("local[2]")
    val sc = new SparkContext(conf)

    /*返回 (url,1)*/
    val rdd0 = sc.textFile("E://BigData//Scala//practice//itcast.log").map(line => {
      val f = line.split("\t")
      (f(1),1)
    })
    /*(rdd1,ArrayBuffer((http://php.itcast.cn/php/course.shtml,459), (http://java.itcast.cn/java/course/base.shtml,543), (http://java.itcast.cn/java/video.shtml,496), (http://java.itcast.cn/java/course/android.shtml,501), (http://net.itcast.cn/net/video.shtml,521), (http://java.itcast.cn/java/course/hadoop.shtml,506), (http://net.itcast.cn/net/course.shtml,521), (http://java.itcast.cn/java/course/cloud.shtml,1028), (http://php.itcast.cn/php/video.shtml,490), (http://java.itcast.cn/java/teacher.shtml,482), (http://php.itcast.cn/php/teacher.shtml,464), (http://net.itcast.cn/net/teacher.shtml,512), (http://java.itcast.cn/java/course/javaee.shtml,1000), (http://java.itcast.cn/java/course/javaeeadvanced.shtml,477)))
*/
    val rdd1 = rdd0.reduceByKey(_+_)
    /*(rddb,ArrayBuffer((http://php.itcast.cn/php/course.shtml,459), (http://java.itcast.cn/java/video.shtml,496), (http://java.itcast.cn/java/course/base.shtml,543), (http://java.itcast.cn/java/course/android.shtml,501), (http://net.itcast.cn/net/video.shtml,521), (http://java.itcast.cn/java/course/hadoop.shtml,506), (http://net.itcast.cn/net/course.shtml,521), (http://java.itcast.cn/java/course/cloud.shtml,1028), (http://php.itcast.cn/php/video.shtml,490), (http://java.itcast.cn/java/teacher.shtml,482), (http://php.itcast.cn/php/teacher.shtml,464), (http://net.itcast.cn/net/teacher.shtml,512), (http://java.itcast.cn/java/course/javaee.shtml,1000), (http://java.itcast.cn/java/course/javaeeadvanced.shtml,477)))
*/
    /*val rdda = rdd0.groupByKey()
    val rddb = rdda.map(t => {
      val size = t._2.size
      (t._1,size)
    })
    println("rddb",rddb.collect().toBuffer)*/


    /*统计不同子域名下的数量*/
    val rdd2 = rdd1.map(t => {
      val url = t._1
      /*Java中的得到host的方法*/
      val host = new URL(url).getHost
      (host,url,t._2)
    })
    /*(rdd2,ArrayBuffer((php.itcast.cn,http://php.itcast.cn/php/course.shtml,459), (java.itcast.cn,http://java.itcast.cn/java/course/base.shtml,543), (java.itcast.cn,http://java.itcast.cn/java/video.shtml,496), (java.itcast.cn,http://java.itcast.cn/java/course/android.shtml,501), (net.itcast.cn,http://net.itcast.cn/net/video.shtml,521), (java.itcast.cn,http://java.itcast.cn/java/course/hadoop.shtml,506), (net.itcast.cn,http://net.itcast.cn/net/course.shtml,521), (java.itcast.cn,http://java.itcast.cn/java/course/cloud.shtml,1028), (php.itcast.cn,http://php.itcast.cn/php/video.shtml,490), (java.itcast.cn,http://java.itcast.cn/java/teacher.shtml,482), (php.itcast.cn,http://php.itcast.cn/php/teacher.shtml,464), (net.itcast.cn,http://net.itcast.cn/net/teacher.shtml,512), (java.itcast.cn,http://java.itcast.cn/java/course/javaee.shtml,1000), (java.itcast.cn,http://java.itcast.cn/java/course/javaeeadvanced.shtml,477)))
*/
    println("rdd2",rdd2.collect().toBuffer)

    //从数据库中加载规则
    val arr = Array("java.itcast.cn", "php.itcast.cn", "net.itcast.cn")
    for (ins <- arr) {
      /*对应host子域名下进行前几名排序*/
      val rdd3 = rdd2.filter(_._1 == ins)
      /*默认为TRUE，升序，FALSE为降序*/
      val rdd4 = rdd3.sortBy(_._3,false).take(3)
      /*(rdd4,ArrayBuffer((java.itcast.cn,http://java.itcast.cn/java/course/cloud.shtml,1028), (java.itcast.cn,http://java.itcast.cn/java/course/javaee.shtml,1000), (java.itcast.cn,http://java.itcast.cn/java/course/base.shtml,543)))
*/
      /*(rdd4,ArrayBuffer((php.itcast.cn,http://php.itcast.cn/php/video.shtml,490), (php.itcast.cn,http://php.itcast.cn/php/teacher.shtml,464), (php.itcast.cn,http://php.itcast.cn/php/course.shtml,459)))
*/
      println("rdd4",rdd4.toBuffer)
    }

    sc.stop()
  }
}
