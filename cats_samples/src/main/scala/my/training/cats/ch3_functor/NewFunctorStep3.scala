package my.training.cats.ch3_functor

import cats.Functor
import cats.syntax.functor._
import cats.instances.option._
import cats.instances.int._

object NewFunctorStep3 {

  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    override def map[A, B](value: Option[A])(f: A => B): Option[B] =
      value.map(f)
  }

  def main(args: Array[String]): Unit = {
    val opt1: Option[String] = Option("123")
    val opt2: Option[Int] =  optionFunctor.map(opt1)(_.toInt)

    println("opt1: " + opt1)
    println("opt2: " + opt2)
  }
}
