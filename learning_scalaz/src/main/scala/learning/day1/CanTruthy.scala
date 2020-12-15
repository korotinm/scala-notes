package learning.day1

trait CanTruthy[A] { self =>
  def truthys(a: A): Boolean
}

object CanTruthy {
  def apply[A](implicit ev: CanTruthy[A]): CanTruthy[A] = ev

  def thruthys[A](f: A => Boolean): CanTruthy[A] = new CanTruthy[A] {
    def truthys(a: A): Boolean = f(a)
  }

  def truthyIf[A: CanTruthy, B, C](cond: A)(ifyes: => B)(ifno: => C) = {
    val ct = implicitly[CanTruthy[A]]
    if (ct.truthys(cond)) ifyes
    else ifno
  }
}

trait CanTruthyOps[A] {
  def self: A
  implicit def F: CanTruthy[A]
  final def truthy: Boolean = F.truthys(self)
}

object ToCanTruthyOps {

  implicit def toCanIsThruthyOps[A](v: A)(implicit ev: CanTruthy[A]) = new CanTruthyOps[A] {
    def self = v
    implicit def F = ev
  }
}

object Main extends App {
  import ToCanTruthyOps._

  //int truthy
  implicit val intCanTruthy: CanTruthy[Int] = CanTruthy.thruthys({
    case 0 => false
    case _ => true
  })

  println(s"10.truthy: ${10.truthy}")

  //list truthy
  implicit def listCanThruthy[A]: CanTruthy[List[A]] =
    CanTruthy.thruthys[List[A]]({
      case Nil => false
      case _   => true
    })

  implicit val nilCanTruthy: CanTruthy[scala.collection.immutable.Nil.type] =
    CanTruthy.thruthys[scala.collection.immutable.Nil.type](_ => false)

  println(s"List.empty[Int].truthy: ${List.empty[Int].truthy}")
  println(s"""List("a", "b", "c").truthy: ${List("a", "b", "c").truthy}""")
  println(s"""Nil.truthy: ${Nil.truthy}""")

  //boolean truthy
  implicit val boolCanThruthy: CanTruthy[Boolean] = CanTruthy.thruthys[Boolean](identity)

  println(s"""true.truthy: ${true.truthy}""")
  println(s"""false.truthy: ${false.truthy}""")

  //truthy if
  println(s"""CanTruthy.truthyIf(10.truthy){"YES"}{"NO"}: ${CanTruthy.truthyIf(10.truthy){"YES"}{"NO"}}""")
  println(s"""CanTruthy.truthyIf(List(1, 2, 3).truthy){"YES"}{"NO"}: ${CanTruthy.truthyIf(List(1, 2, 3)){"List is defined"}{"List is empty"}}""")
}
