package my.training.akka.step1

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object CommonImmutableData {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("common_resource")
    val actor = system.actorOf(Props[CommonActor], name = "common_actor")

    (1 to 500).foreach(v =>
      if(v % 2 == 0)
        Future(actor ! "add1")
      else Future(actor ! "add2")
    )

    Future(
      while (true) {
        TimeUnit.SECONDS.sleep(2)
        actor ! "size1"
        actor ! "size2"
      }
    )

    TimeUnit.MINUTES.sleep(2)
    system.terminate()
  }
}

class CommonActor extends Actor {

  var map: Map[Int, String] = _

  override def preStart(): Unit =
    map = Map(1 -> "", 2 -> "")


  override def receive: Receive = {
    case "add1" =>
      println(s"time1=${System.currentTimeMillis()}")
      map = map.updated(1, map(1) + "a")
    case "add2" =>
      println(s"time2=${System.currentTimeMillis()}")
      map = map.updated(2, map(2) + "b")
    case "size1" =>
      println(s"size1 = ${map(1).length}")
    case "size2" =>
      println(s"size2 = ${map(2).length}")
    case "print" =>
      println(map.mkString)



  }
}

