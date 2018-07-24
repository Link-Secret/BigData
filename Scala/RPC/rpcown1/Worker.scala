package cn.itcast.rpcown1

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/21 8:57
  * \* Description: RPC通信 Worker，向Master发送信息
  * \*/
class Worker(val masterHost : String, val masterPort : Int) extends Actor {

  var master : ActorSelection = _

  override def preStart(): Unit = {
    //在preStart阶段   跟Master建立连接
    master = context.actorSelection(s"akka.tcp://MasterSystem@$masterHost:$masterPort/user/Master")
    master ! "connect"
  }

  override def receive: Receive = {
    case "reply" => {
      println("receive reply")
    }
  }
}


/*两个Actor通信，需要 IP port ActorSystem Actor*/
object Worker {
  def main(args: Array[String]): Unit = {
    /*通信 IP 端口设置 以及 */
    val host = args(0)
    val port = args(1).toInt
    val masterHost = args(2)
    val masterPort = args(3).toInt
    /*配置*/
    val configStr = {
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    }
    /*解析配置*/
    val config = ConfigFactory.parseString(configStr)
    /*ActorSystem老大，辅助创建和监控下面的Actor，他是单例的*/
    val actorSystem = ActorSystem("WorkerSystem",config)  //当前ActorSystem起名WorkerSystem
    /*创建Actor*/
    actorSystem.actorOf(Props(new Worker(masterHost,masterPort)), "Worker")
    /*让这个进程等待结束*/
    actorSystem.awaitTermination()
  }
}
