package my.training.cats.ch4_monad.transformation.exercise

import cats.data.EitherT
import cats.syntax.either._
import cats.instances.either._
import cats.instances.future._
import cats.syntax.flatMap._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object TransformAndRollOut {

  type Response[A] = EitherT[Future, String, A]

  val powerLevels = Map(
    "Jazz"      -> 6,
    "Bumblebee" -> 8,
    "Hot Rod"   -> 10
  )

  def getPowerLevel(autobot: String): Response[Int] =
    powerLevels.get(autobot) match {
      case Some(v) => EitherT.right(Future(v))
      case None => EitherT.left(Future(s"$autobot unreachable."))
    }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] =
    for {
      a <- getPowerLevel(ally1)
      b <- getPowerLevel(ally2)
    } yield (a + b) > 15


  def tacticalReport(ally1: String, ally2: String): String =
    Await.result(canSpecialMove(ally1, ally2).value, 5 seconds) match {
      case Left(errMsg) =>
        errMsg
      case Right(false) =>
        s"$ally1 and $ally2 need a recharge!"
      case Right(true) =>
        s"$ally1 and $ally2 are ready to roll out!"
    }

  def main(args: Array[String]): Unit = {
    println(tacticalReport("Jazz", "Bumblebee"))

    println(tacticalReport("Bumblebee", "Hot Rod"))

    println(tacticalReport("Jazz", "Ironhide"))

  }

}

/* console output:

  Jazz and Bumblebee need a recharge!
  Bumblebee and Hot Rod are ready to roll out!
  Ironhide unreachable.

 */
