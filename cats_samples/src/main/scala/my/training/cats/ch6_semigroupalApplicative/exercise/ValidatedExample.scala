package my.training.cats.ch6_semigroupalApplicative.exercise

import cats.data.Validated

import cats.syntax.either._
import cats.instances.list._
import cats.syntax.apply._

case class User(name: String, age: Int)

object ValidatedExample {

  type Form = Map[String, String]
  type FailFast[A] = Either[List[String], A]
  type FailSlow[A] = Validated[List[String], A]

  def getValue(name: String)(form: Form): FailFast[String] =
    form.get(name)
      .toRight(List(s"name was not found by name=$name"))

  def parseInt(name: String)(data: String): FailFast[Int] =
    Either.catchOnly[NumberFormatException](data.toInt)
      .leftMap(ex => List(s"$name cannot parse into age: ${ex.getMessage}"))

  def nonBlank(name: String)(data: String): FailFast[String] =
    Right(data)
      .ensure(List(s"$name cannot be empty"))(_.nonEmpty)

  def nonNegative(name: String)(data: Int): FailFast[Int] =
    Right(data)
      .ensure(List(s"$name must be non-negative"))(_ >= 0)


  def getName(form: Form) =
    getValue("name")(form).flatMap(nonBlank("name"))

  def getAge(form: Form) =
    getValue("age")(form)
      .flatMap(nonBlank("age"))
      .flatMap(parseInt("age"))
      .flatMap(nonNegative("age"))


  def readUser(form: Form): FailSlow[User] =
    (
      getName(form).toValidated,
      getAge(form).toValidated
    ).mapN(User.apply)

  def main(args: Array[String]): Unit = {
    val res = readUser(Map("name" -> "Bob", "age" -> "15"))
    println("res: " + res)

    val res2 = readUser(Map("name" -> "Bob", "age" -> "-15"))
    println("res2: " + res2)

    val res3 = readUser(Map("name" -> "", "age" -> "-15"))
    println("res3: " + res3)

    val res4 = readUser(Map("name" -> "Bob", "age" -> ""))
    println("res4: " + res4)

    val res5 = readUser(Map("name" -> "", "age" -> ""))
    println("res5: " + res5)

    val res6 = readUser(Map("name" -> "Bob", "age" -> "15 age"))
    println("res6: " + res6)
  }
}

/* console output:

res: Valid(User(Bob,15))
res2: Invalid(List(age must be non-negative))
res3: Invalid(List(name cannot be empty, age must be non-negative))
res4: Invalid(List(age cannot be empty))
res5: Invalid(List(name cannot be empty, age cannot be empty))
res6: Invalid(List(age cannot parse into age: For input string: "15 age"))

 */