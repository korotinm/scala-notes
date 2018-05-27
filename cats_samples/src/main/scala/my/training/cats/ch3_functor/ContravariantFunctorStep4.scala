package my.training.cats.ch3_functor

import my.training.cats.ch3_functor.common.Box

object ContravariantFunctorStep4 {

  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

  implicit def boxPrintable[A](implicit p: Printable[A]) =
    p.contramap[Box[A]](_.value)

  implicit val stringPrintable = new Printable[String] {
    override def format(input: String) = "\"" + input + "\""
  }

  implicit val booleanPrintable = new Printable[Boolean] {
    override def format(input: Boolean) = if(input) "yes" else "no"
  }

  def main(args: Array[String]): Unit = {
    //warm-up
    println(format("Are you ready?"))
    println(format(true).capitalize + "!")

    //using contramap
    println(format(Box("Are you ready?")))
    println(format(Box(true)).capitalize + "!")
  }

}

trait Printable[A] {self =>
  def format(input: A): String

  def contramap[B](func: B => A): Printable[B] =
    new Printable[B] {
      override def format(input: B): String = {
        self.format(func(input))
      }
    }
}
