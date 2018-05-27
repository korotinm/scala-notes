package my.training.cats.ch6_semigroupalApplicative

import my.training.cats.ch6_semigroupalApplicative.common.Person

object SemigroupalStep1 extends App {


  def product {
    import cats.Semigroupal
    import cats.instances.option._
    import cats.syntax.option._

    println("\n---product---\n")

    val optTpl = Semigroupal[Option].product(15.some, true.some)
    println("optTpl: " + optTpl)

    val noneTpl = Semigroupal[Option].product(15.some, None)
    println("noneTpl: " + noneTpl)

    val tupled = Semigroupal.tuple3(15.some, true.some, "day".some)
    println("tupled: " + tupled)

    val map3 = Semigroupal.map3(2.some, 3.some, 10.some)(_ + _ + _)
    println("map3: " + map3)

  }

  product

  def applySyntax {
    import cats.instances.option._
    import cats.syntax.apply._
    import cats.syntax.option._

    println("\n---applySyntax---\n")

    val tupled = (10.some, 5.some).tupled
    println("tupled: " + tupled)

    val mapN = ("Ivan".some, 25.some, true.some).mapN(Person.apply)
    println("mapN: " + mapN)

  }

  applySyntax

}

/* console output:

---product---

optTpl: Some((15,true))
noneTpl: None
tupled: Some((15,true,day))
map3: Some(15)

---applySyntax---

tupled: Some((10,5))
mapN: Some(Person(Ivan,25,true))

 */