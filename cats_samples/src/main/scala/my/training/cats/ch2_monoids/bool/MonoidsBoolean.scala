package my.training.cats.ch2_monoids.bool

import my.training.cats.ch2_monoids.Monoid


object MonoidsBoolean {
  implicit val monoidAnd = new Monoid[Boolean] {
    override def empty = true
    override def combine(x: Boolean, y: Boolean) = x && y
  }

  implicit val monoidOr = new Monoid[Boolean] {
    override def empty = false
    override def combine(x: Boolean, y: Boolean) = x || y
  }


  def main(args: Array[String]): Unit = {
    import my.training.cats.ch2_monoids.CheckMonoid._

    println(associativeLaw[Boolean](true, false, true)(monoidAnd))
    println(associativeLaw[Boolean](true, false, true)(monoidOr))

    println(identityLaw[Boolean](true)(monoidAnd))
    println(identityLaw[Boolean](false)(monoidOr))
  }
}
