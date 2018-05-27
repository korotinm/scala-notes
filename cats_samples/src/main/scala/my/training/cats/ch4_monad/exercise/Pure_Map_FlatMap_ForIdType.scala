package my.training.cats.ch4_monad.exercise

import cats.Id

object Pure_Map_FlatMap_ForIdType {

  def pure[A](v: A): Id[A] = v

  def flatMap[A, B](v: Id[A])(f: A => Id[B]): Id[B] =
    f(v)

  def map[A, B](v: Id[A])(f: A => B): Id[B] =
    f(v)

  def main(args: Array[String]): Unit = {

    println("pure for Id type: " + pure(15))

    println("map Id type: " + map(5)(_ * 2))

    println("flatMap Id type: " + flatMap(5)(_ * 2))

  }

}


