package my.training.cats.ch6_semigroupalApplicative.exercise

import cats.Monad
import cats.syntax.functor._
import cats.syntax.flatMap._

object ImplementProductAsFlatMap extends App{

  def product[M[_]: Monad, A, B](x: M[A], y: M[B]): M[(A, B)] =
    for{
      a <- x
      b <- y
    } yield (a, b)

}
