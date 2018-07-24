package cn.itcast.rpcown2

/**
  * \* Created with IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/23 9:40
  * \* Description: 
  * \*/
trait RemoteMessage extends Serializable

/*1. 定义worker -> master id 内存 核数*/  /*走网络通信必须要序列化*/
case class RegisterWorker(id : String, memory : Int,  cores : Int) extends RemoteMessage

/*2. Master -> worker*/
case class RegisteredWorker(masterUrl : String) extends RemoteMessage

/*3. Worker -> self  自己给自己发心跳消息*/
case object SendHeartbeat

/*4. worker -> master 心跳*/
case class Heartbeat(id : String) extends RemoteMessage

/*5. Master -> self 超时检测*/
case object CheckTimeOutWorker