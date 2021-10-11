import scala.util.Failure
import scala.util.Success
import java.util.concurrent.TimeUnit
import java.util.ArrayList
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import _root_.scala.collection.BitSet
import scala.util.Random

class Container[A](value: A) {
  def add(v: Int)(implicit ev: A =:= Int) = value + v
}

new Container(1).add(2)

def testSubTypingEvidence[A, B](a: A, b: B)(implicit ev: B <:< A) = (a, b)

//testSubTypingEvidence(1, List(2,3))

//----
{
  class ~:~[A, B]

  object ~:~ {
    implicit def eqTypeEvidence[A]: ~:~[A, A] = new ~:~[A, A]
  }

  //import ~:~._
  def testEvidence[A, B](a: A, b: B)(implicit ev: A ~:~ B): Tuple2[A, A] = (a, b.asInstanceOf[A])
  testEvidence(3, 2)
}

{
  class <<:<<[-A, +B]
  object <<:<< {
    implicit def subTypeEvidence[A]: <<:<<[A, A] = new <<:<<[A, A]
  }
  import <<:<<._

  trait Person
  class Boss extends Person
  case class CEO() extends Boss
  case class Stuff() extends Person

  def testEvidence[A, B](a: A, b: B)(implicit ev: A <<:<< B) = (a, b)
  testEvidence(CEO(), new Boss)
}

//-- Trampoline
sealed trait Trampoline[A] {

  def run: A = this match {
    case Done(a) => a
    case More(a) => a().run
  }
}

case class Done[A](a: A) extends Trampoline[A]
case class More[A](a: () => Trampoline[A]) extends Trampoline[A]

def even(n: Int): Trampoline[Boolean] =
  n match {
    case 0 => Done(true)
    case _ => More(() => odd(n - 1))
  }

def odd(n: Int): Trampoline[Boolean] =
  n match {
    case 0 => Done(false)
    case _ => More(() => even(n - 1))
  }

odd(21).run

//----PartialFunction
def checkDouble: PartialFunction[Double, Double] = {
  case d if d > 0 && d < 11 => d
}

def checkDouble2: PartialFunction[Double, Double] = {
  case d if d >= 10 => d
}

checkDouble.orElse(checkDouble2).apply(12.0)

checkDouble.applyOrElse(12.0, checkDouble2)

checkDouble.compose(checkDouble2)(10)

List(0.0, 1.0, 2.0, 3.0, 10.0).collect(checkDouble)

//------Extractor

object CustomerId {
  def apply(name: String): String = s"$name----${java.util.UUID.randomUUID().toString()}"

  def unapply(customerId: String): Option[String] = {
    val arr = customerId.split("----")
    arr.tail.nonEmpty match {
      case true  => Some(arr.head)
      case false => None
    }
  }
}

val cId = CustomerId("mikhail")
cId
cId match {
  case CustomerId(name) => name
  case _                => "Could not extract customer id"
}

//------- Variances
trait Animal
class Dog extends Animal
class Cat extends Animal

abstract class ContrvType[-T] {
  def typeName: Unit
}

class SuperType extends ContrvType[Animal] {
  override def typeName: Unit = println("SuperType")
}

class SubTypeDog extends ContrvType[Dog] {
  override def typeName: Unit = println("SubType Dog")
}

class SubTypeCat extends ContrvType[Cat] {
  override def typeName: Unit = println("SubType Cat")
}

class TestContrv {
  def display(v: ContrvType[Dog]) = v.typeName
}

val testContrv = new TestContrv
testContrv.display(new SubTypeDog)
//testContrv.display(new SubTypeCat)
testContrv.display(new SuperType)

//+T -> (S subtype of T) AND (List[S] subtype of List[T])
//-T -> (S subtype of T) AND (List[T] subtype of List[S])

//-T -> (Int subtype of AnyVal) AND List[AnyVal] <: List[Int]

trait Function[-A, +B] {
  def apply(a: A): B
}

//---- List
trait LList[+T] { self =>

  def reverse: LList[T] = {
    def reverseList(acc: LList[T], xs: LList[T]): LList[T] = xs match {
      case NNil             => acc
      case Cons(head, tail) => reverseList(Cons(head, acc), tail)
    }
    reverseList(NNil, self)
  }
}

object LList {

  def apply[T](items: T*): LList[T] =
    if (items.isEmpty) NNil
    else Cons(items.head, apply(items.tail: _*))
}

case object NNil extends LList[Nothing]
case class Cons[T](head: T, tail: LList[T]) extends LList[T]

LList(1, 2, 3, 4, 5, 6).reverse

//------Semigroup, Functor, Monoid, Monad
trait Semigroup[A] {
  def mappend(a: A, b: => A): A
}

val semi = new Semigroup[Int] {
  def mappend(a: Int, b: => Int): Int = a + b
}

semi.mappend(1, semi.mappend(2, 5)) == semi.mappend(semi.mappend(1, 2), 5)

//[F
trait Functor[F[_]] {
  def pure[A](a: A): F[A]
  def map[A, B](a: F[A])(b: A => B): F[B]

  /*
  Functor.map(identity) == Functor
  Functor.map(f).map(g) == Functor.map(a => g(f(a)))


 */
}

new Functor[Option] {
  def pure[A](a: A): Option[A] = Option(a)
  def map[A, B](a: Option[A])(f: A => B): Option[B] = a.map(f)
}
//F]

//[M
trait Monad[F[_]] {
  def pure[A](a: A): F[A]
  def map[A, B](a: F[A])(f: A => B): F[B]
  def flatMap[A, B](a: F[A])(f: A => F[B]): F[B]

  /*
  Monad.pure(a).flatMap(f) == f(a)
  Monad.flatMap(pure) == Monad
  Monad.flatMap(f).flatMap(g) == Monad.flatMap(a => f(a).flatMap(g))
 */
}
//M]

//[Type Classes
trait Appendable[A] {
  def append(a: A, b: A): A
}

object Appendable {

  implicit val appendableInt = new Appendable[Int] {
    override def append(a: Int, b: Int): Int = a + b
  }

  implicit val appendableString = new Appendable[String] {
    override def append(a: String, b: String): String = a + b
  }
}

def appendItems[A](a: A, b: A)(implicit ev: Appendable[A]) =
  ev.append(a, b)

// with context bound
def appendItemsV2[A: Appendable](a: A, b: A) = {
  val ev = implicitly[Appendable[A]]
  ev.append(a, b)
}

import Appendable._
appendItems(1, 2) // : Int = 3
//Type Classes]

//[Lens
case class Lense[A, B](
    get: A => B,
    set: (A, B) => A
)

case class Street(address: String, number: Int)

val streetNumberLens = Lense[Street, Int](
  get = _.number,
  set = (a, b) => a.copy(number = b)
)

val street = Street("Pushkin street", 12)

streetNumberLens.get(street)
streetNumberLens.set(street, 15)

object Lense {

  def compose[Outer, Inner, Value](outer: Lense[Outer, Inner], inner: Lense[Inner, Value]) = Lense[Outer, Value](
    get = outer.get andThen inner.get,
    set = (obj, value) => outer.set(obj, inner.set(outer.get(obj), value))
  )
}

case class Person(name: String, street: Street)

val personLens = Lense[Person, Street](
  get = _.street,
  set = (a, b) => a.copy(street = b)
)

val personStreetLens: Lense[Person, Int] = Lense.compose(personLens, streetNumberLens)

val person = Person(name = "Ivan", street = street)

personStreetLens.get(person)
personStreetLens.set(person, 17)

//Lens]

