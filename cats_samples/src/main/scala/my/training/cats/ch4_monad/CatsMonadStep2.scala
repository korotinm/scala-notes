package my.training.cats.ch4_monad

import cats.Monad
import cats.instances.option._
import cats.instances.list._

object CatsMonadStep2 extends App {

  //Monad[Option]:

  val optMonad = Monad[Option].pure(7)
  println("optMonad: " + optMonad)

  val optMonad2 = Monad[Option].flatMap(optMonad)(v => Some(v + 2))
  println("optMonad2: " + optMonad2)

  val optMonad3 = Monad[Option].map(optMonad2)(v => v * 2)
  println("optMonad3: " + optMonad3)


  //Monad[List]:

  val listMonad = Monad[List].pure(3)
  println("listMonad: " + listMonad)

  val listMonad2 = Monad[List].flatMap(List(1,2,3))(v => List(v * 2))
  println("listMonad2: " + listMonad2)

  val listMonad3 = Monad[List].map(listMonad2)(v => v * v)
  println("listMonad3: " + listMonad3)

  val listIfM = Monad[List].ifM(List(true, true, false, true))(List(1), List(0))
  println("listIfM: " + listIfM)


}
