package my.training.cats.ch4_monad


//import cats.instances.either._
import cats.syntax.either._

import scala.util.Try

object CatsEitherStpe3 {

  //scala style: compile error
  /*def coundPositive(nums: List[Int]) =
    nums.foldLeft(Right(0))((acc, v) =>
      if(v > 0)
        acc.map(_ + 1)
      else
        Left(s"Value $v is negative")
    )*/

  //cats feature
  def countPositive(nums: List[Int]) =
    nums.foldLeft[Either[String, Int]](0.asRight) { (acc, v) =>
      if (v > 0)
        acc.map(_ + 1)
      else
        Left(s"Value $v is negative")

    }

  def main(args: Array[String]): Unit = {
    println(countPositive(List(1, 2, 3)))
    //Right(3)
    println(countPositive(List(1, 2, -3)))
    //Left(Value -3 is negative)

    println(Either.catchOnly[NumberFormatException]("number".toInt))
    //Left(java.lang.NumberFormatException: For input string: "number")

    println(Either.catchNonFatal(sys.error("Non fatal error")))


    println(Either.catchNonFatal("15".toInt))
    //Right(123)

    println(Either.fromTry(Try("15D".toInt)))


    println(Either.fromOption[String, Int](None, "Value is none"))
    //Left(Value is none)

    println(Either.fromOption[String, Int](Some(15), "Value is none"))

    println("Error".asLeft[Int].getOrElse(15))

    println("Error".asLeft[Int].orElse(15.asRight[String]))

    println(-15.asRight[String].ensure("value is negative")(_ > 0))
    //Left(value is negative)

    println(
      "Achtung!".asLeft[Int].recover {
        case s: String => 15
      }
    )

    println("!gnuthcA".asLeft.leftMap(_.reverse))

    println(15.asRight[String].bimap(_.reverse, _ * 5))

    println(15.asRight[String].swap)
    //Left(15)

    println(15.asRight[String].toOption)

    println(15.asRight[String].toList)
    //List(15)
    println("error".asLeft[Int].toList)
    //List()

    println(15.asRight.toValidated.isValid)
    //true
  }

}
