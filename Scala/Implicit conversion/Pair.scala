package cn.itcast.implic

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/23 19:56
  * \* Description: 
  * \*/
class Pair[T <: Comparable[T]]{

  def bigger(first : T, second : T) = {
      if (first.compareTo(second) > 0) first else second
  }
}


object Pair {
  def main(args: Array[String]): Unit = {

    val p = new Pair[Integer]
//    println(p.bigger("hadoop", "spark"))    /*String*/
    println(p.bigger(1, 2))                   /*Integer*/
  }
}