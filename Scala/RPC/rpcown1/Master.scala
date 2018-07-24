package cn.itcast.rpcown1

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/21 8:57
  * \* Description: RPC通信 Master用于接收信息
  * \*/
class Master extends Actor{

  /*构造函数 */
  println("construct !")

  /*在接收消息之前会调用preStart()方法*/
  override def preStart(): Unit = {
    println("preStart invoked")
  }

  /*用于接收消息*/
  override def receive: Receive = {
    case "connect" => {
      println("a client collected")
      /*接收消息 发送反馈 异步不用返回消息*/
      sender ! "reply"
    }
    case "hello" => {
      println("hello")
    }
  }
}


object Master {

  def main(args: Array[String]): Unit = {
      val host = args(0)
      val port = args(1).toInt
      /*准备配置*/
      val configStr =
        s"""
           |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
           |akka.remote.netty.tcp.hostname = "$host"
           |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
      /*解析配置*/
      val config  = ConfigFactory.parseString(configStr)
      /*ActorSystem*/
      val actorSystem  = ActorSystem("MasterSystem", config)
      /*创建Actor*/
      val master = actorSystem.actorOf(Props(new Master), "Master")
      /*自己给自己发送消息测试*/
      master ! "hello"
      /*让这个进程等待结束*/
      actorSystem.awaitTermination()
  }
}