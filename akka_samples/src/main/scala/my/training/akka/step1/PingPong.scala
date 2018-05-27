package my.training.akka.step1

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import scala.concurrent.duration._

object PingPong {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("pingpong")
    val pinger = system.actorOf(Props[Pinger], "pinger")
    val ponger = system.actorOf(Props(classOf[Ponger], pinger), "ponger")

    import system.dispatcher
    system.scheduler.scheduleOnce(500 millis) {
      ponger ! Ping
    }
  }

}

case object Ping

case object Pong

class Pinger extends Actor {

  var countDown: Int = 100

  override def receive: Receive = {
    case Pong =>
      if (countDown % 10 == 0)
        println(s"pong: ${self.path} count down $countDown")
      if (countDown > 0) {
        countDown -= 1
        sender() ! Ping

      } else {
        sender() ! PoisonPill
        self ! PoisonPill
      }

  }
}

class Ponger(pinger: ActorRef) extends Actor {
  override def receive: Receive = {
    case Ping =>
      println(s"ping: ${self.path} time id ${System.nanoTime() / 10000}")
      pinger ! Pong
  }
}