package my.training.cats.ch3_functor



import cats.syntax.eq._
import cats.Functor
import cats.instances.option._
import cats.instances.int._

import scala.language.higherKinds

object FunctorStep1 {

  def main(args: Array[String]): Unit = {
    val opt: Option[Int] = Option(123)
    val opt1: Option[Int] = Option(123)
    val opt2: Option[String] = Functor[Option].map(opt1)("'" + _.toString + "'")

    println(opt1)
    println(opt2)

    println("" + (opt === opt1))

    //lift method
    val func1 = (x: Int) => x * x
    val funcOpt1 = Functor[Option].lift(func1)
    println("funcOpt1: " + funcOpt1(Some(3)))


  }

}
