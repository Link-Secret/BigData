package cn.itcast.rpcown2

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/23 10:40
  * \* Description: 
  * \*/
class WorkerInfo(val id : String, val memory : Int, val cores : Int) {

  //TODO 上一次心跳
  var lastHeartbeatTime : Long = _
}
