package my.training.cats.ch4_monad

import scala.language.higherKinds

trait MonadStep1[F[_]] {

  def pure[A](v: A): F[A]

  def flatMap[A, B](value: F[A])(f: A => F[B]): F[B]

  def map[A, B](value: F[A])(f: A => B): F[B] =
    flatMap(value)(vA => pure(f(vA)))

}
