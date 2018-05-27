package my.training.akka.stop_actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}


class X extends Actor {
  override def receive: Receive = {
    case _ =>
      println("dummy print")
  }
}

class DeathWatchActor extends Actor{

  var child: ActorRef = context.actorOf(Props[X], "child")

  context.watch(child)

  var lastSender = context.system.deadLetters

  override def receive: Receive = {
    case "task"              =>
      println("process of the task ...")
      child ! "x"
    case "kill"              =>
      context.stop(child)
      lastSender = sender()
    case Terminated(chld) =>
      println(s"path of child actor: ${chld.path}")
      lastSender ! "finished"
  }

}

object DeathWatchActor {
  def main(args: Array[String]): Unit = {

    val system = ActorSystem("DeathWatch")
    val deathWatchAct = system.actorOf(Props[DeathWatchActor], name = "death_watch")

    deathWatchAct ! "task"
    deathWatchAct ! "kill"


  }
}
