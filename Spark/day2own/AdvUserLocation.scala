package cn.itcast.spark.day2own

import org.apache.spark.{SparkConf, SparkContext}

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/26 8:34
  * \* Description: 
  * \*/
object AdvUserLocation {

  def main(args: Array[String]): Unit = {
    /*程序运行配置*/
    val conf = new SparkConf().setAppName("AdvUserLocation").setMaster("local[2]")
    val sc = new SparkContext(conf)

    /*ArrayBuffer(((18688888888,16030401EAFB68F1E3CDF819735E1C66),20160327082400), ((18611132889,16030401EAFB68F1E3CDF819735E1C66),20160327082500),*/
    val rdd0 = sc.textFile("E://BigData//Scala//practice//bs_log").map(line => {
      val fields = line.split(",")
      val eventType = fields(3)
      val time = fields(1)
      val timeLong = if (eventType == "1") -time.toLong else time.toLong
      ((fields(0), fields(2)), timeLong)
    })
    println("rdd0", rdd0.collect().toBuffer)


    /*ArrayBuffer((CC0710CC94ECC657A8561DE549D940E0,(18688888888,1300)), (9F36407EAD0629FC166F14DDE7970F68,(18611132889,54000)),*/
    val rdd1 = rdd0.reduceByKey(_ + _).map(t => {
      val mobile = t._1._1
      val lac = t._1._2
      val time = t._2
      (lac, (mobile, time))
    })
    println("rdd1", rdd1.collect().toBuffer)


    /*ArrayBuffer((CC0710CC94ECC657A8561DE549D940E0,(18688888888,1300)), (CC0710CC94ECC657A8561DE549D940E0,(18611132889,1900)),*/
    val rdd2 = rdd1.sortBy(_._2._2)
    println("rdd2", rdd2.collect().toBuffer)


    /*ArrayBuffer((18688888888,List((CC0710CC94ECC657A8561DE549D940E0,(18688888888,1300)), (9F36407EAD0629FC166F14DDE7970F68,(18688888888,51200)), (16030401EAFB68F1E3CDF819735E1C66,(18688888888,87600)))), (18611132889,List((CC0710CC94ECC657A8561DE549D940E0,(18611132889,1900)), (9F36407EAD0629FC166F14DDE7970F68,(18611132889,54000)), (16030401EAFB68F1E3CDF819735E1C66,(18611132889,97500)))))*/
    val rddT = rdd2.map(t => {
      (t._1, t._2._1, t._2._2)
    })
    val rdd3 = rddT.groupBy(_._2)
    val rdd4 = rdd3.mapValues(it => {
      it.toList.sortBy(_._2).take(10)
    })
    println("rdd4", rdd4.collect().toBuffer)


    sc.stop()
  }
}
