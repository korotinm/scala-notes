import scala.reflect.ClassTag

// functor
trait Functor[F[_]] {
  def pure[A](a: A): F[A]
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

/* rules

    1) identity law
    Functor[T].map(identity) == Functor[T]

    2) assoc. law
    Functor[T].map(f).map(g) == Functor[T].map(a => g(f(a)))
 */

//###

//monad
trait Monad[F[_]] extends Functor[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

/* rules

    1) left unit
    pure(x).flatMap(f) == f(x)

    2) right unit
    Monad[T].flatMap(pure) == Monad[T]

    3) assoc. law
    Monad[T].flatMap(f).flatMap(g) == Monad[T].flatMap(a => f(a).flatMap(g))

 */

//implementing Lits
trait LList[+T] {

  def reverse: LList[T] = {
    def _reverse(acc: LList[T], xs: LList[T]): LList[T] =
      xs match {
        case NNil        => acc
        case Cons(x, xs) => _reverse(Cons(x, acc), xs)
      }

    _reverse(NNil, this)
  }
}

object LList {
  def apply[T](x: T): LList[T] = Cons(x, NNil)
  def apply[T](x: T, xs: LList[T]): LList[T] = Cons(x, xs)

  def apply[T](x: T*): LList[T] =
    if (x.isEmpty) NNil
    else Cons(x.head, apply(x.tail: _*))
}

case object NNil extends LList[Nothing]
case class Cons[T](x: T, xs: LList[T]) extends LList[T]

LList(1, 2, 3, 4, 5).reverse

//ClassTag
val map = Map[Int, Any](1 -> 10, 2 -> "test", 3 -> 15.1)

def testCt[T: ClassTag](key: Int, map: Map[Int, Any]): Option[T] =
  map.get(key) match {
    case Some(v: T) => Some(v)
    case _          => None
  }
testCt[Double](3, map)

// tagged type
{
  object TaggedType {
    type Tagged[U] = { type Tag = U }
    type @@[T, U] = T with Tagged[U]
  }

  import TaggedType._

  trait Day
  type DayTime = Long @@ Day

  def DayTime(v: Long): DayTime = v.asInstanceOf[DayTime]
  println(DayTime(10L) + DayTime(11L))
  List(DayTime(1), DayTime(2), DayTime(3)).sortWith((f, s) => f > s)

}


