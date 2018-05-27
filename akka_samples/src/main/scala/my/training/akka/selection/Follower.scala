package my.training.akka.selection

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorIdentity, ActorRef, ActorSystem, Identify, Props, Terminated}

class Follower extends Actor {

  val id = 1

  context.actorSelection("/user.another") ! Identify(id)

  override def receive: Receive = {
    case ActorIdentity(`id`, Some(ref)) =>
      println("ActorIdentity with some ref")
      context.watch(ref)
      context.become(active(ref))
    case ActorIdentity(`id`, None) =>
      println("ActorIdentity with none ref")
      context.stop(self)

    case "msg" =>
      println("msg")

  }

  def active(another: ActorRef): Actor.Receive = {
    case Terminated(`another`) =>
      context.stop(self)
  }
}


object Follower {
  def main(args: Array[String]): Unit = {
    val sys = ActorSystem("Follower")
    val actor = sys.actorOf(Props[Follower], name = "follower")

    //begin code here
    TimeUnit.SECONDS.sleep(1)
    //end   code here

    actor ! "msg"

  }
}