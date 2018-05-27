package my.training.cats.ch2_monoids.set

import my.training.cats.ch2_monoids.Monoid

object MonoidsSet {
  def main(args: Array[String]): Unit = {
    import my.training.cats.ch2_monoids.CheckMonoid._

    {
      implicit def monoidAllSet[A] = new Monoid[Set[A]] {
        override def empty = Set.empty[A]
        override def combine(x: Set[A], y: Set[A]) = x union y
      }

      println(monoidAllSet.combine(Set(1, 2), Set(2, 3)))
      println(associativeLaw[Set[Int]](Set(1, 2, 3), Set(2, 3, 4), Set(3, 4, 5)))
    }

    println("--------------")

    {

      implicit def monoidAllSet[A] = new Monoid[Set[A]] {
        override def empty = Set.empty[A]
        override def combine(x: Set[A], y: Set[A]) = x union y
      }

      implicit val intMonoid = new Monoid[Int] {
        override def empty = 0
        override def combine(x: Int, y: Int) = x + y
      }

      val intSetMonoid = Monoid[Set[Int]]
      println(intSetMonoid.combine(Set(1, 2), Set(2, 3)))
    }
  }
}
