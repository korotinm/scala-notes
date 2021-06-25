import zio.Runtime
import zio.console.Console
import zio.clock.Clock
import zio.random.Random
import zio.actors.Actor.Stateful
import zio.actors._
import zio.UIO
import zio.ZIO
import zio.Task
import zio.Schedule
import java.util.concurrent.TimeUnit
import scala.collection.immutable
import zio.Fiber
import zio.URIO
import java.lang.Thread.State
import zio.RIO


object Example1 extends scala.App {
  implicit val rt: Runtime[Clock with Console with Random] = Runtime.default

  val stateful = new Stateful[Any, Unit, Command] { self =>

    override def receive[A](state: Unit, msg: Command[A], context: Context): UIO[(Unit, A)] =
      msg match {
        case DoubleCommand(value) =>
          if (value % 100000 == 0) println(s"!")
          UIO(((), value * 2))
      }
  }

  val t =
    for {
      system <- ActorSystem("mySystem")
      actor <- system.make("actor1", Supervisor.none, (), stateful)

      actor2 <- system.make("actor2", Supervisor.none, (), stateful)

      seqTask0 = (1 to 2000000).map(i => actor2 ! DoubleCommand(i))

      seqTask = (1 to 2000000).map(i => actor ! DoubleCommand(i))
      
      res <- Task.collectAll(seqTask ++ seqTask0)
    } yield res

  rt.unsafeRun(t)
}

sealed trait Command[+_]
case class DoubleCommand(value: Int) extends Command[Int]
