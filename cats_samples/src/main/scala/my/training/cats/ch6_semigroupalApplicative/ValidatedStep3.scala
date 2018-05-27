package my.training.cats.ch6_semigroupalApplicative

import scala.util.Try

object ValidatedStep3 extends App {

  def simple {
    import cats.data.Validated
    import cats.syntax.validated._

    println("\n---simple---\n")

    val valid = 15.valid[List[String]]
    println("valid: " + valid)

    val invalid = List("error1", "error2").invalid[Int]
    println("invalid: " + invalid)

    val catchOnly =  Validated.catchOnly[NumberFormatException]("test".toInt)
    println("catchOnly: " + catchOnly)

    val catchNonFatal = Validated.catchNonFatal(sys.error("err"))
    println("catchNonFatal: " + catchNonFatal)

    val fromTry = Validated.fromTry(Try(15/0))
    println("fromTry: " + fromTry)

    val fromEither =  Validated.fromEither[String, Int](Right(15))
    println("fromEither: " + fromEither)


  }
  simple

  def combine {
    import cats.data.Validated
    import cats.instances.vector._
    import cats.syntax.validated._
    import cats.syntax.apply._

    println("\n---combine---\n")

    val tupledVector = (Vector(404), Vector(500)).tupled
    println("tupledVector: " + tupledVector)

    val valid = 15.valid.map(_ * 2)
    println("valid: " + valid)

    val validBimap = 15.valid[String].bimap(_ + "!", _ * 2)
    println("validBimap: " + validBimap)

    val invalidBimap = "?".invalid[Int].bimap(_ + "!", _ * 2)
    println("invalidBimap: " + invalidBimap)

    val withEither = 14.valid[String].withEither(_.flatMap(n => Right(n + 1)))
    println("withEither: " + withEither)

  }
  combine

}

/* console output:

---simple---

valid: Valid(15)
invalid: Invalid(List(error1, error2))
catchOnly: Invalid(java.lang.NumberFormatException: For input string: "test")
catchNonFatal: Invalid(java.lang.RuntimeException: err)
fromTry: Invalid(java.lang.ArithmeticException: / by zero)
fromEither: Valid(15)

---combine---

tupledVector: Vector((404,500))
valid: Valid(30)
validBimap: Valid(30)
invalidBimap: Invalid(?!)
withEither: Valid(15)

 */