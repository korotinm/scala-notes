package my.training.cats.ch4_monad

object CatsFunctorAndFlatMapStep2_2 extends App {

  import cats.Monad
  import cats.syntax.functor._
  import cats.syntax.flatMap._
  import scala.language.higherKinds

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    a.flatMap(x => b.map(y => x*x + y*y))


  import cats.instances.option._
  import cats.syntax.option._
  import cats.instances.list._


  println("1) sumSquare option: " + sumSquare(Option(2), Option(3)))
  println("2) sumSquare option: " + sumSquare(Option(2), none[Int]))

  println("1) sumSquare list: " + sumSquare(List(2,3), List(4,5)))


  import cats.Id

  println("1) sumSquare Id: " + sumSquare(2: Id[Int], 3: Id[Int]))

  val idInt: Id[Int] = 4
  println("Id[Int] = " + idInt)


}
