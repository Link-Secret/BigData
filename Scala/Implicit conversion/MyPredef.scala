package cn.itcast.implic

import java.io.File

/**
  * Created by root on 2016/5/13.
  */
object MyPredef {
  implicit def fileToRichFile(f: File)  = new RichFile(f)

  /*from cn.itcast.implic.Girl => Ordered[cn.itcast.implic.Girl].*/
  implicit def girl2Ordered(g: Girl) = new Ordered[Girl] {
    override def compare(that: Girl): Int = {
      g.faceValue - that.faceValue
    }
  }

}
