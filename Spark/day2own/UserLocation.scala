package cn.itcast.spark.day2own

import org.apache.spark.{SparkConf, SparkContext}

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/25 18:54
  * \* Description: 
  * \*/
object UserLocation {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("AdvUserLocation").setMaster("local[2]")
    val sc = new SparkContext(conf)


    /*ArrayBuffer((18688888888_16030401EAFB68F1E3CDF819735E1C66,-20160327082400),(18611132889_16030401EAFB68F1E3CDF819735E1C66,-20160327082500)...*/
    val rdd0 = sc.textFile("E://BigData//Scala//practice//bs_log").map(line => {
        val fileds = line.split(",")
        val eventType = fileds(3)   //断开还是连接的字段
        val time = fileds(1)        //时间字段
        val timeLong = if (eventType == "1") -time.toLong else time.toLong //如果是建立连接则是负
        //返回值
        (fileds(0)+"_"+fileds(2),timeLong)
    })
    println(rdd0.collect().toBuffer)


    /*ArrayBuffer((18688888888,CompactBuffer((18688888888,9F36407EAD0629FC166F14DDE7970F68,51200), (18688888888,CC0710CC94ECC657A8561DE549D940E0,1300), (18688888888,16030401EAFB68F1E3CDF819735E1C66,87600))),*/
    val rdd1 = rdd0.groupBy(_._1).mapValues(_.foldLeft(0L)(_+_._2))
    val rdd2 = rdd1.map(t => {
      val mobile_bs = t._1
      val mobile = mobile_bs.split("_")(0)
      val lac = mobile_bs.split("_")(1)
      val stepTime = t._2
      (mobile,lac,stepTime)
    })
    val rdd3 = rdd2.groupBy(_._1)
    println(rdd3.collect().toBuffer)

    /*ArrayBuffer((18688888888,List((18688888888,16030401EAFB68F1E3CDF819735E1C66,87600), (18688888888,9F36407EAD0629FC166F14DDE7970F68,51200)*/
    val rdd4 = rdd3.mapValues(it => {
      /*从高到低 取3个来排序*/
      it.toList.sortBy(_._3).reverse.take(3)
    })
    println(rdd4.collect().toBuffer)

    /*停止*/
    sc.stop()
  }

}
