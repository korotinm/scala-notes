package my.training.akka.stash

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorSystem, Props, Stash}

object StashMain {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("system-check-stash")
    val actor = system.actorOf(Props[StashActor], name = "stash")

    (1 to 500).foreach(v =>
      actor ! System.nanoTime()
    )

    actor ! Unstash
    actor ! Close
    actor ! Terminate

  }
}

class StashActor extends Actor with Stash/*or UnboundedStash*/{

  private val counter = new AtomicInteger(1)

  override def receive: Receive = {
    case ns: Long if ns % 2 == 0 =>
      stash()
    case ns: Long =>
      println(s"#${counter.getAndIncrement()} odd ns: $ns")
    case Unstash =>
      unstashAll()
      context.become({

        case ns: Long =>
          println(s"#${counter.getAndIncrement()} even ns: $ns")

        case Close =>
          context.unbecome()

      }, discardOld = false)
    case Terminate =>
      println(s"terminating system: ${context.system.name}")
      context.system.terminate()
  }

}

case object Terminate
case object Unstash
case object Close