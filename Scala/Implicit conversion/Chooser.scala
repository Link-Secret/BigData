package cn.itcast.implic

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/24 10:26
  * \* Description: 
  * \*/
/*视图界定   [T <% M]关系意味着T可以被隐式转换成M[T]*/
class Chooser[T <% Ordered[T]] {

    def choose(first : T, second : T) : T = {
      if (first > second) first else second
    }
}


object Chooser {
  def main(args: Array[String]): Unit = {
    import cn.itcast.implic.MyPredef.girl2Ordered
    val c = new Chooser[Girl]
    val g1 = new Girl("g1", 11)
    val g2 = new Girl("g2", 22)


    val g = c.choose(g1,g2)
    println(g.faceValue + g.name)
  }
}
