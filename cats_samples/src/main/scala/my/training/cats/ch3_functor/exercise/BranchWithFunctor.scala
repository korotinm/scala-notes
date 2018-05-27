package my.training.cats.ch3_functor.exercise

import cats.Functor
import cats.syntax.functor._

object BranchWithFunctor {

  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B) =
      fa match {
        case Branch(l, r) => Branch(map(l)(f), map(r)(f))
        case Leaf(v) => Leaf(f(v))
      }

  }

  def main(args: Array[String]): Unit = {
    val branch = Tree(Leaf(5), Leaf(15)).map(v => v * 2)
    val leaf = Tree(10).map(_ * 3)

    println("branch: " + branch)
    println("leaf: " + leaf)

    //begin: check composition rule
    val f: Int => Int = (x: Int) => x * 2
    val g: Int => Int = (x: Int) => x * -1

    val tree1 = Tree(Leaf(5), Leaf(15)).map(f).map(g)
    val tree2 = Tree(Leaf(5), Leaf(15)).map(v => g(f(v)))

    println("tree1: " + tree1)
    println("tree2: " + tree2)
    println("is tree1 == tree2: " + (tree1 == tree2))

    //end
  }


}

sealed trait Tree[+A]

object Tree {
  def apply[A](value: A): Tree[A] = Leaf(value)
  def apply[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
}

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]
