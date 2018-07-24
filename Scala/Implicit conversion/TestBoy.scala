package cn.itcast.implic

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/24 10:01
  * \* Description: 
  * \*/
object TestBoy {

  def main(args: Array[String]): Unit = {
    /*Java -- 的思路 重写compareTo方法*/
    val b1 = new Boy("b1", 11)
    val b2 = new Boy("b2", 22)

    val arr = Array[Boy](b1,b2)
    val sorted = arr.sortBy[Boy](x => x).reverse
    for (b <- sorted) {
      println(b.name)
    }

  }
}
