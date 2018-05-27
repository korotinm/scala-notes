package my.training.cats.ch6_semigroupalApplicative

import my.training.cats.ch6_semigroupalApplicative.common.{Person, Person2}

object FunctorAndApplyStep2 extends App {

  def implImap {
    import cats.Monoid
    import cats.instances.int._
    import cats.instances.invariant._
    import cats.instances.string._
    import cats.syntax.apply._
    import cats.syntax.semigroup._

    println("\n---implImap---\n")

    val tupleToPerson: (String, Int) => Person2 = Person2.apply

    val personToTuple: Person2 => (String, Int) =
      p => (p.name, p.age)

    implicit val pMonoid: Monoid[Person2] = (
      Monoid[String],
      Monoid[Int]
    ).imapN(tupleToPerson)(personToTuple)

    val p1 = Person2("Mike", 15)
    val p2 = Person2("Kitty", 15)
    println("p1 |+| p2: " + (p1 |+| p2))

  }

  implImap

}

/* console output:

---implImap---

p1 |+| p2: Person2(MikeKitty,30)

 */
