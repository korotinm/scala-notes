package my.training.cats.ch3_functor

import scala.language.higherKinds
import cats.Functor
import cats.syntax.functor._
import cats.instances.option._
import cats.instances.list._
import cats.instances.int._

object FunctorStep2 {

  def doMath[F[_]](v: F[Int])(implicit functor: Functor[F]): F[Int] =
    v.map(n => n + 2)


  def main(args: Array[String]): Unit = {
    println("doMath(Option(10)): " + doMath(Option(10)))
    println("doMath(List(1, 2, 3)): " + doMath(List(1, 2, 3)))
  }

}
