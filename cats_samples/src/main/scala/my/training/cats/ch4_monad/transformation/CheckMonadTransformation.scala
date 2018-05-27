package my.training.cats.ch4_monad.transformation

import java.util.concurrent.TimeUnit

import cats.data.{EitherT, OptionT}
import cats.instances.future._
import cats.instances.option._
import cats.instances.either._
import cats.syntax.applicative._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object CheckMonadTransformation extends App{

  type ErrorOr[A] = Either[String, A]
  type ErrorOrOption[A] = OptionT[ErrorOr, A]

  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]

  val feo = 10.pure[FutureEitherOption]
  println("feo: " + feo)

  val feoMap = feo.map(_ * 2)
  val resultFeoMap = Await.result(feoMap.value.value, 200 millis)
  println("resultFeoMap: "  + resultFeoMap)

  val fe = 15.pure[FutureEither]
  val resultFe = Await.result(fe.value, 200 millis)
  println("resultFe: " + resultFe)

  val errorOr = 15.pure[ErrorOrOption]
  println("errorOr: " + errorOr)

  val errorPure: FutureEither[Unit] = EitherT.leftT("error")
  println("errorPure: " + errorPure)

  val futureSucc = EitherT(Future[Either[String, Int]]{println("running"); TimeUnit.SECONDS.sleep(3); Right(10)})
  TimeUnit.MILLISECONDS.sleep(100)
  println("futureSucc: " + futureSucc)
}

/* console output:
  feo: OptionT(EitherT(Future(Success(Right(Some(10))))))
  resultFeoMap: Right(Some(20))
  resultFe: Right(15)
  errorOr: OptionT(Right(Some(15)))
  errorPure: EitherT(Future(Success(Left(error))))
  running
  futureSucc: EitherT(Future(<not completed>))
 */
