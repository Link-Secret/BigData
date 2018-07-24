package cn.itcast.rpcown2

/**
  * \* Created with IntelliJ IDEA.
  * \* Author: acer zjl
  * \* Date: 2018/7/23 9:46
  * \* Description: 
  * \*/
import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable

class Master(val host : String, val port : Int) extends Actor{

  /*保存注册worker信息 key为String类型的id，value为封装的workerID，memory，cores的workerInfo
  * workerID -> workerInfo 的映射 */
  val idToWorker = new mutable.HashMap[String, WorkerInfo]()
  /*WorkerInfo*/
  val workers = new mutable.HashSet[WorkerInfo]()

  /*Master检测worker是否存活的检测时间  超时检测*/
  val CHECK_INTERVAL = 15000

  /*构造函数 */
  println("construct !")

  /*在接收消息之前会调用preStart()方法*/
  override def preStart(): Unit = {
    println("preStart invoked")
    /*Master 只要一启动就检测 Worker是否存活 */
    import scala.concurrent.duration._
    /*定时器的隐式转换需要导入*/
    import context.dispatcher
    context.system.scheduler.schedule(0 millis, CHECK_INTERVAL millis, self, CheckTimeOutWorker)
  }

  /*用于接收消息*/
  override def receive: Receive = {
    /*接收注册信息*/
    case RegisterWorker(id, memory, cores) => {
      /*判断是否注册*/
      if(!idToWorker.contains(id)) {
        /*将worker的信息封装起来保存到内存中 spark中还将其持久化到zk文件系统中*/
        val workerInfo = new WorkerInfo(id, memory, cores)
        /**/
        idToWorker(id) = workerInfo
        workers += workerInfo

        /*Master回应 Worker 自己的地址URI*/
        sender ! RegisteredWorker(s"akka.tcp://MasterSystem@$host:$port/user/Master")
      }
    }
    /*4. 接收worker发送的心跳*/
    case Heartbeat(id) => {
      if(idToWorker.contains(id)) {
        /*取出workerID对应的value*/
        val workerInfo = idToWorker(id)
        /*报活*/
        val currentTime = System.currentTimeMillis()
        workerInfo.lastHeartbeatTime = currentTime
      }
    }
    /*5. master接收到自己的超时检测信息*/
    case CheckTimeOutWorker => {
      /*删除已经超时的worker*/
      val currentTime = System.currentTimeMillis()
      val toRemove = workers.filter(x => currentTime - x.lastHeartbeatTime > CHECK_INTERVAL)
      for(w <- toRemove) {
        /*删除worker的set集合中超时Worker*/
        workers -= w
        /*删除id -> workerInfo 映射中的超时键值对*/
        idToWorker -= w.id
      }
      /*打印出存活的Workers的集合*/
      println(workers.size)
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
    val master = actorSystem.actorOf(Props(new Master(host, port)), "Master")
    /*自己给自己发送消息测试*/
//    master ! "hello"
    /*让这个进程等待结束*/
    actorSystem.awaitTermination()
  }
}