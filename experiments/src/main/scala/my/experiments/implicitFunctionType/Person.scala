package my.experiments.implicitFunctionType

import scala.language.implicitConversions


trait FuncImplicitType1[-T, +R] extends AnyRef {self =>
  def apply(implicit p: T): R
}

class Person(val name: String, val age: Int)


object Person {

  //todo: implemented in dotty
  /*type Personal[T] = implicit Person => T
  def person: Personal[Person] = implicitly[Person]*/

}

object PersonTest {


  def getNameV1(person: Person): String =
    person.name

  def getNameV2(implicit person: Person): String =
    person.name

  //todo: use dotty
  //import Person._
  //def getNameV3: Personal[String] =



  def main(args: Array[String]): Unit = {
    val person = new Person("Jhony", 25)
  }
}