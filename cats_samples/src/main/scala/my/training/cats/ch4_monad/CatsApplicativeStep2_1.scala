package my.training.cats.ch4_monad

import cats.instances.option._
import cats.syntax.applicative._

object CatsApplicativeStep2_1 extends App {

  println("applicative method pure: " + 1.pure[Option])

}
