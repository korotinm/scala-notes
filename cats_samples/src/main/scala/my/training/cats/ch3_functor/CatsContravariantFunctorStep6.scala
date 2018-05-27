package my.training.cats.ch3_functor

import cats.Contravariant
import cats.Show
import cats.instances.string._
import cats.syntax.contravariant._

object CatsContravariantFunctorStep6 {

  val showString = Show[String]

  val showSymbol = Contravariant[Show].contramap(showString)((sym: Symbol) => s"'${sym.name}")

  def main(args: Array[String]): Unit = {
    println(showSymbol.show('test))

    println(showString.contramap[Symbol](_.name).show('test_symbol))
  }

}
