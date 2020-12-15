package learning.day0

//monoid
trait Monoid[A] {
  def mappend(a: A, b: A): A
  def mzero: A
}

object Monoid {

  implicit val IntMonoid: Monoid[Int] = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  implicit val MultiIntMonoid: Monoid[Int] = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a * b
    def mzero: Int = 1
  }

  implicit val StringMonoid: Monoid[String] = new Monoid[String] {
    def mappend(a: String, b: String): String = a + b
    def mzero: String = ""
  }

  implicit val BooleanMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    def mappend(a: Boolean, b: Boolean): Boolean = a | b
    def mzero: Boolean = false
  }
}

//monoid op.
trait MonoidOp[A] {
  val F: Monoid[A]
  val value: A
  def |+|(a2: A): A = F.mappend(value, a2)
  def some: Option[A] = Option(value)
}

object MonoidOp {

  implicit def toMonoidOpt[A: Monoid](a: A): MonoidOp[A] = new MonoidOp[A] {
    val F = implicitly[Monoid[A]]
    val value = a
  }
}

//fold left
trait FoldLeft[F[_]] {
  def foldLeft[A, B](x: F[A], b: B, f: (B, A) => B): B
}

object FoldLeft {

  implicit val ListFoldLeft: FoldLeft[List] = new FoldLeft[List] {
    def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B): B = xs.foldLeft(b)(f)
  }
}

object Main extends App {
  { //Monoid and FoldLeft
    import Monoid._
    import FoldLeft.ListFoldLeft

    def sum[FL[_]: FoldLeft, A: Monoid](xs: FL[A]): A = {
      val m = implicitly[Monoid[A]]
      val fl = implicitly[FoldLeft[FL]]
      fl.foldLeft(xs, m.mzero, m.mappend)
    }

    println(s"SUM string: ${sum(List("a", "b", "c"))}")
    println(s"SUM int: ${sum(List(1, 2, 3))(ListFoldLeft, IntMonoid)}")
    println(s"MUL int: ${sum(List(2, 2))(ListFoldLeft, MultiIntMonoid)}")
  }

  //---MonoidOp
  {
    //import Monoid.IntMonoid
    import Monoid.MultiIntMonoid
    import MonoidOp.toMonoidOpt

    println(s""" "a" |+| "b" |+| "c": ${"a" |+| "b" |+| "c"} """)
    println(s""" 2 |+| 8 |+| 5: ${2 |+| 8 |+| 5} """)
    println(s""" "abc".some: ${"abc".some} """)
  }
}
