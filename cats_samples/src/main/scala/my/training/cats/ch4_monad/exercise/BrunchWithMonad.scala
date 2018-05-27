package my.training.cats.ch4_monad.exercise

import cats.Monad

import scala.annotation.tailrec

class BrunchWithMonad {

  implicit val treeMonad = new Monad[Tree] {
    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] =
      fa match {
        case Branch(left, right) =>
          Branch(flatMap(left)(f), flatMap(right)(f))
        case Leaf(v) => f(v)
      }


    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = ???
     /* f(a) match {
        case Branch(l, r) =>
          Branch(

          )
      }*/

    override def pure[A](x: A) = Leaf(x)
  }

}

sealed trait Tree[+A]

object Tree {
  def apply[A](value: A): Tree[A] = Leaf(value)
  def apply[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
}

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]
