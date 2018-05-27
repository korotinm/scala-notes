package my.training.cats.ch4_monad.exercise

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import cats.data.Writer
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._

object WriterFactorial {

  type Log[A] = Writer[Vector[String], A]

  def slowly[A](body: => A) =
    try body finally Thread.sleep(100)

  def factorial(n: Int): Log[Int] =
    for {
      res <-
      if (n == 0)
        1.pure[Log]
      else
        slowly(factorial(n - 1).map(_ * n))
      _ <- Vector(s"fact $n $res").tell

    } yield res

  def main(args: Array[String]): Unit = {
    Await.result(Future.sequence(Vector(
      Future(println(factorial(3).written)),
      Future(println(factorial(5).written)),
      Future(println(factorial(7).written)),
      Future(println(factorial(9).written)),
      Future(println(factorial(11).written))
    )), 5.seconds)
  }

}
