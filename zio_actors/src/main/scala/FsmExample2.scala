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

import zio.RIO
import zio.IO
import zio.console._

object FsmExample2 extends scala.App {
  implicit val rt: Runtime[Clock with Console with Random] = Runtime.default

  val fsm = new Actor.Stateful[Any, State, Command] { self =>
    override def receive[A](state: State, msg: Command[A], context: Context): IO[SaleException, (State, A)] =
      state match {
        case Opened =>
          msg match {
            case Open =>
              for {
                _ <- Console.Service.live.putStrLn("Opening ...")
                res <- IO.effectTotal((Opened, ()))
              } yield res
            case Close =>
              for {
                _ <- Console.Service.live.putStrLn("Closing ...")
                res <- IO.effectTotal((Closed, ()))
              } yield res
            case Sell =>
              IO.effectTotal((Opened, "Selling ..."))
          }
        case Closed =>
          msg match {
            case Open =>
              for {
                _ <- Console.Service.live.putStrLn("Opening ...")
                res <- IO.effectTotal((Opened, ()))
              } yield res
            case Close =>
              IO.effectTotal((Closed, ()))
            case Sell =>
              IO.fail(SaleException("Shop is closed"))
          }
      }
  }

  val t =
    for {
      system <- ActorSystem("mySystem")
      actor <- system.make("actor1", Supervisor.none, Opened, fsm)
      actor2 <- system.make("actor2", Supervisor.none, Closed, fsm)
      res0 <- actor ! Open
      res1 <- actor ! Close
      res2 <- (actor2 ! Sell)

      r <- Task.apply((res0, res1, res2))
    } yield r

  rt.unsafeRun(t)

  sealed trait Command[+A]
  case object Open extends Command[Unit]
  case object Close extends Command[Unit]
  case object Sell extends Command[String]

  sealed trait State
  case object Opened extends State
  case object Closed extends State

  case class SaleException(msg: String) extends Throwable
}
//trait ZFSM[R, S, -F[+_]] extends Actor.Stateful[R, S, F]
