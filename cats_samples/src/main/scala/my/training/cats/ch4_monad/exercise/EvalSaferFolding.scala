package my.training.cats.ch4_monad.exercise

import cats.Eval

object EvalSaferFolding extends App {

  def foldRight[A, B](as: List[A], acc: B)(fn: (A, Eval[B]) => Eval[B]): Eval[B] =
    as match {
      case head :: tail =>
        Eval.defer(fn(head, foldRight(tail, acc)(fn)))
      case Nil =>
        Eval.now(acc)
    }


  def foldRightNotSafer[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    as match {
      case head :: tail =>
        fn(head, foldRightNotSafer(tail, acc)(fn))
      case Nil =>
        acc
    }


  val res = foldRight((1 to 1000000).toList, 0)((a, b) => b.map(_ + a))
  println("Safer foldRight: " + res.value)

  val notSaferRes = foldRightNotSafer((1 to 1000000).toList, 0)(_ + _)
  println("Not safer foldRight: " + notSaferRes)
}
