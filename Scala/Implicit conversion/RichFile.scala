package cn.itcast.implic

import java.io.File

import scala.io.Source

import MyPredef._

/**
  * Created by root on 2016/5/13.
  */



/**/
class RichFile(val f: File) {
  def read() = Source.fromFile(f).mkString
}

object RichFile {
  def main(args: Array[String]) {
    val f = new File("c://words.txt")
    //装饰模式 ，显示的增强
    val contents = new RichFile(f).read()
//    val contents = f.read()
    println(contents)

    /*隐式转换  隐式的增强*/
    import cn.itcast.implic.MyPredef.fileToRichFile
    f.read()
    println(f.read())
  }
}


