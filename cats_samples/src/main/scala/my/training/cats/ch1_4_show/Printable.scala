package my.training.cats.ch1_4_show

import my.training.cats.Cat

trait Printable[A] {
  def format(input: A): String
}

object PrintableInstances {
  implicit val stringPrintable = new Printable[String] {
    override def format(input: String) = input
  }

  implicit val intPrintable = new Printable[Int] {
    override def format(input: Int) = input.toString
  }

  implicit val catPrintable = new Printable[Cat] {
    override def format(input: Cat) = s"${input.name.toUpperCase} is a ${input.age} year-old ${input.color.toUpperCase} cat"
  }
}

object Printable {
  def format[A](input: A)(implicit p: Printable[A]): String =
    p.format(input)

  def print[A](input: A)(implicit p: Printable[A]): Unit =
    println(p.format(input))
}

object PrintableSyntax {

  implicit class PrintableOps[A](value: A) {
    def format(implicit p: Printable[A]): String =
      p.format(value)

    def print(implicit p: Printable[A]): Unit =
      println(p.format(value))
  }

}

object PrintableMain {
  def main(args: Array[String]): Unit = {
    import PrintableInstances._
    import PrintableSyntax._

    Printable.print(10)
    Printable.print("test")
    Printable.print(Cat("Anton", 12, "blue"))

    Cat("anton", 6, "light blue").print

    10.print
  }
}
