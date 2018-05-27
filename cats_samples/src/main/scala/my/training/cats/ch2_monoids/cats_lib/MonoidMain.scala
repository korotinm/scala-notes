package my.training.cats.ch2_monoids.cats_lib

import cats.Monoid
import cats.instances.int._
import cats.instances.string._
import cats.instances.option._
import cats.instances.set._
import cats.instances.tuple._
//import cats.syntax.monoid._
import cats.syntax.semigroup._

object MonoidMain extends App{

  println("int:\n" + (1 |+| 2))

  println("int:\n" + (1 |+| 2 |+| Monoid[Int].empty))

  println("set:\n" + (Set("a", "b") |+| Set("c", "a")))

  println("option:\n" + (Option(1) |+| Option(3) |+| Monoid[Option[Int]].empty))


  val tpl1 = ("test", 1)
  val tpl2 = (" number ", 1)
  println("tuple:\n" + (tpl1 |+| tpl2))
}
