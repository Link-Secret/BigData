package cn.itcast.implic

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/24 9:45
  * \* Description: 
  * \*/
/*type compable takes type parameters   --  Comparable[Any]如果没有添加Any保错*/
class Boy(val name : String, var faceValue : Int) extends Comparable[Boy]{
  override def compareTo(o: Boy): Int = {
    this.faceValue - o.faceValue
  }
}
