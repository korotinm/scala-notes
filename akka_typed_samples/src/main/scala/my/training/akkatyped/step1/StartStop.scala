package my.training.akkatyped.step1

import akka.actor.typed.ActorSystem
import akka.actor.typed.{Behavior, PostStop}
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors
import java.util.concurrent.TimeUnit

object StartStop extends App {
  val actorSys = ActorSystem(StartStopActor1(), "startStopSystem")
  actorSys ! "stop"

  TimeUnit.SECONDS.sleep(1)

  actorSys.terminate
}

class StartStopActor1(context: ActorContext[String]) extends AbstractBehavior[String] {

  println("first started")

  val secondRef = context.spawn(StartStopActor2(), "second")

  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "stop" =>
        secondRef ! "msg"
        Behaviors.stopped
    }

  override def onSignal = {
    case PostStop =>
      println("first stopped")
      this
  }
}

object StartStopActor1 {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new StartStopActor1(context))
}

class StartStopActor2(context: ActorContext[String]) extends AbstractBehavior[String] {

  println("second started")

  override def onMessage(msg: String): Behavior[String] = {
    Behaviors.unhandled
  }

  override def onSignal = {
    case PostStop =>
      println("second stopped")
      this
  }
}

object StartStopActor2 {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new StartStopActor2(context))
}
