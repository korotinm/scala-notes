package learning.day4

trait Monoid[A]

trait Foldable[F[_]] {
  def foldMap[A, B](fa: F[A])(f: A => B)(implicit F: Monoid[B]): B
  def foldRight[A, B](fa: F[A], z: => A)(f: (A, => B) => B): B
}
