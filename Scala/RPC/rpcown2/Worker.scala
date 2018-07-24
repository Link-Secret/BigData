package cn.itcast.rpcown2

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/23 9:47
  * \* Description: 
  * \*/
import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class Worker(val masterHost : String, val masterPort : Int, val memory : Int, val cores : Int) extends Actor {

  var master : ActorSelection = _
  /*注册WorkerID定义*/
  val workerId = UUID.randomUUID().toString
  /*Worker向Master发送心跳的间隔*/
  val HEART_INTERVAL = 10000

  override def preStart(): Unit = {
    println("connect worker -> master")
    //在preStart阶段   跟Master建立连接
    master = context.actorSelection(s"akka.tcp://MasterSystem@$masterHost:$masterPort/user/Master")
    /*worker向master发送注册信息 ,注册信息中包含workID，内存，核数*/
    master ! RegisterWorker(workerId, memory, cores)
  }

  override def receive: Receive = {
    /*接收 Master -> Worker 的反馈*/
    case RegisteredWorker(masterUrl) => {
      println(masterUrl)
      /*启动定时器 定时发送心跳*/
      import scala.concurrent.duration._
      /*需要导入定时器的隐式转换*/
      import context.dispatcher
      //                                                                  先自己给自己发消息
      context.system.scheduler.schedule(0 millis,  HEART_INTERVAL millis, self, SendHeartbeat)
    }
    /*接收自己发送给自己的心跳消息  再发送给master心跳信息*/
    case SendHeartbeat => {
      println("send Heartbeat to master")
      master ! Heartbeat(workerId)
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
    /*worker向master注册字段*/
    val memory = args(4).toInt
    val cores = args(5).toInt
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
    actorSystem.actorOf(Props(new Worker(masterHost, masterPort, memory, cores)), "Worker")
    /*让这个进程等待结束*/
    actorSystem.awaitTermination()
  }
}
