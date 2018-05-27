package my.training.cats.ch3_functor

import cats.Monoid
import cats.instances.string._
import cats.syntax.invariant._
import cats.syntax.semigroup._

object CatsInvariantFunctorStep7 {

  def main(args: Array[String]): Unit = {
    implicit val symbolMonoid: Monoid[Symbol] =
      Monoid[String].imap(Symbol.apply)(_.name) 

    println("empty symbol: " + Monoid[Symbol].empty)
    println("combine symbol: " + Monoid[Symbol].combine('sy, 'mbol))
    println("combine symbol |+|: " + ('sym |+| 'bol))
  }

}
