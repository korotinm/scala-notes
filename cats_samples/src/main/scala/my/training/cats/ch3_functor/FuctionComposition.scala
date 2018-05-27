package my.training.cats.ch3_functor

import cats.instances.function._
import cats.syntax.functor._


object FuctionComposition {
  def main(args: Array[String]): Unit = {
    val f1: Int => Double = (i: Int) => i * i * 1.0
    val f2: Double => String = (d: Double) => d.toString.reverse

    val res1 = f1.map(f2)(2)
    println("res1 = " + res1)

    val res2 = (f1 map f2)(2)
    println("res2 = " + res2)

    val res3 = (f1 andThen f2)(2)
    println("res3 = " + res3)
  }
}
