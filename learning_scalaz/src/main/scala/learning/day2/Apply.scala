package learning.day2

trait Functor[F[_]] { self =>
  def map[A, B](fa: => F[A])(f: A => B): F[B]
}

trait Apply[F[_]] extends Functor[F] { self =>
  def ap[A, B](fa: => F[A])(f: => F[A => B]): F[B]
}