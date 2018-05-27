package my.training.akka.stop_actors

import akka.actor.{Actor, ActorSystem, Props}

class StopActor extends Actor{

  import context._

  override def receive: Receive = {
    case "a" =>
      println("a")
    case "b" =>
      println("b")
    case "stop" =>
      context stop self
    case Terminate(sys) =>
      sys.terminate()

  }
}

case class Terminate(system: ActorSystem)


object StopActor{
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("StopActor")
    val cancelableActor = system.actorOf(Props[StopActor], name = "cancelable")

    cancelableActor ! "a"
    cancelableActor ! Terminate(system)
    cancelableActor ! "stop"
    cancelableActor ! "b"

  }
}
