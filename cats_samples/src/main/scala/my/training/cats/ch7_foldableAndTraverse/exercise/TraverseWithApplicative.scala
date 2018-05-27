package my.training.cats.ch7_foldableAndTraverse.exercise

import cats.Applicative
import cats.syntax.applicative._
import cats.syntax.apply._

import scala.language.higherKinds

object TraverseWithApplicative extends App{

  def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F])((acc, item) =>
      (acc, func(item)).mapN(_ :+ _)
    )

  def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] =
    listTraverse(list)(identity)


  def exercise1{
    import cats.instances.vector._


    println("\n---exercise1---\n")

    val res = listSequence(List(Vector(1, 2), Vector(3, 4)))
    println("res: " + res)
  }
  exercise1

  def exercise2{
    import cats.instances.vector._


    println("\n---exercise2---\n")

    val res = listSequence(List(Vector(1, 2), Vector(3, 4), Vector(5, 6)))
    println("res: " + res)
  }
  exercise2

  def exercise3{
    import cats.instances.option._


    println("\n---exercise3---\n")

    def process(list: List[Int]) =
      listTraverse(list)(n => if(n % 2 == 0) Some(n) else None)

    val res = process(List(2, 4, 6))
    println("res: " + res)

    val res2 = process(List(2, 3, 6))
    println("res2: " + res2)
  }
  exercise3

}

/*console output:

---exercise1---

res: Vector(List(1, 3), List(1, 4), List(2, 3), List(2, 4))

---exercise2---

res: Vector(List(1, 3, 5), List(1, 3, 6), List(1, 4, 5), List(1, 4, 6), List(2, 3, 5), List(2, 3, 6), List(2, 4, 5), List(2, 4, 6))

---exercise3---

res: Some(List(2, 4, 6))
res2: None


 */