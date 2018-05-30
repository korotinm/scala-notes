package my.training.akka.with_partial_fuction

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

object PartialFunctionChain {
  def main(args: Array[String]): Unit = {
    val sys = ActorSystem("system-partial-fun-chain")
    val actor = sys.actorOf(Props[BehaviorActor], name = "behavior-actor")
    actor ! "step1"
  }
}

trait Behavior {
  this: Actor with ActorLogging =>

  val behaviorReceive: Receive = {
    case "step1" =>
      log.info("\nprocessing ...")

  }
}

class BehaviorActor extends Actor with ActorLogging with Behavior {
  override def receive: Receive = behaviorReceive
}
