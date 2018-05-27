package my.training.akka.hotswap

import akka.actor.{Actor, ActorSystem, Props}

class HotSwapActor extends Actor{

  import context._

  def receiveA: Receive = {
    case "a" =>
      println("receiveA a")
      sender()
    case "b" =>
      println("receiveA b")
      become(receiveB)
  }

  def receiveB: Receive = {
    case "a" =>
      println("receiveB a")
      become(receiveA)
    case "b" =>
      println("receiveB b")
      sender()
  }

  override def receive: Receive = {
    case "a" =>
      println("receive a")
      become(receiveA)
    case "b" =>
      println("receive b")
      become(receiveB)
  }
}

object HotSwapActor {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("HotSwapActor")
    val hotswap = system.actorOf(Props[HotSwapActor], name = "hotswap")

    hotswap ! "a"
    hotswap ! "a"
    hotswap ! "a"
    hotswap ! "b"
    hotswap ! "b"
  }
}
/*console:
receive a
receiveA a
receiveA a
receiveA b
receiveB b
 */