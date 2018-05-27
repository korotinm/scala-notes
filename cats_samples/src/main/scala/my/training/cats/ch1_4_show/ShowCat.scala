package my.training.cats.ch1_4_show

import cats.Show
import cats.syntax.show._
import my.training.cats.Cat

object ShowCat {
  implicit val showCat: Show[Cat] =
    Show.show(cat => s"Cat ${cat.name.capitalize} is ${cat.age} years old with color ${cat.color.toUpperCase}")
}

object ShowMain {
  def main(args: Array[String]): Unit = {
    import ShowCat._
    println(Cat("anton", 13, "light blue").show)
  }
}


