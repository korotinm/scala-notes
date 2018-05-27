package my.training.cats.ch9_mapreduce

import cats.Monoid

import cats.instances.string._
import cats.syntax.semigroup._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import scala.concurrent.{Await, Future}


object ParMapReduce {

  val procs = Runtime.getRuntime.availableProcessors()

  def foldMap[A, B: Monoid](values: Vector[A])(func: A => B): B = {
    values.foldLeft(Monoid[B].empty)((acc, v) => acc |+| func(v))
  }

  def parFoldMap[A, B: Monoid](values: Vector[A])(func: A => B): Future[B] = {
    val count = ((1.0 * values.size) / procs).ceil.toInt

    val futures =
      values
        .grouped(count).toList
        .map(vec => Future(foldMap[A, B](vec)(func)))

    Future.sequence(futures).map(_.foldLeft(Monoid[B].empty)(_ |+| _))
  }
}
