package my.training.cats.ch4_monad



object CatsWriterStep5 extends App {

  def initWriter() {
    import cats.data.Writer

    println("\n---initWriter---\n")

    val vectorWriter = Writer(Vector("a", "b", "c"), 15)
    println(vectorWriter)
  }
  initWriter()

  def smthElse() {
    import cats.data.Writer
    import cats.instances.vector._
    import cats.syntax.applicative._
    import cats.syntax.writer._

    println("\n---smthElse---\n")

    type Log[A] = Writer[Vector[String], A]
    val writer = 15.pure[Log]
    println("writer pure: " + writer)

    val writer2 = writer.tell(Vector("event1"))
    println("writer2: " + writer2)

    val writer3 = writer2.tell(Vector("event2"))
    println("writer3: " + writer3.run)

    println("tell: " + Vector(1,2,3,4,5).tell)

  }
  smthElse()

  def compositionAndTransforming(){
    import cats.data.Writer
    import cats.instances.vector._
    import cats.syntax.applicative._
    import cats.syntax.writer._

    println("\n---compositionAndTransforming---\n")

    type Log[A] = Writer[Vector[String], A]

    val writer1 = for {
      a <- 15.pure[Log]
      _ <- Vector("a", "b", "c").tell
      b <- 15.writer(Vector("x", "y", "z"))
    } yield a// + b

    println("writer1: " + writer1.run)

    val writer2 = writer1.mapWritten(_.map(_.toUpperCase))

    println("writer2: " + writer2.run)

    val writer3 = writer1.bimap(_.map(_.toUpperCase), _ * 2)

    println("writer3: " + writer3.run)

    println("oh f**k: " + {
      for{
        _ <- Vector("q", "w", "e").tell
        a <- writer3

      } yield a
    })

    //reset
    //swap
    //mapBoth
  }
  compositionAndTransforming()


}

/* console output

---initWriter---

WriterT((Vector(a, b, c),15))

---smthElse---

writer pure: WriterT((Vector(),15))
writer2: WriterT((Vector(event1),15))
writer3: (Vector(event1, event2),15)
tell: WriterT((Vector(1, 2, 3, 4, 5),()))

---compositionAndTransforming---

writer1: (Vector(a, b, c, x, y, z),15)
writer2: (Vector(A, B, C, X, Y, Z),15)
writer3: (Vector(A, B, C, X, Y, Z),30)
oh f**k: WriterT((Vector(q, w, e, A, B, C, X, Y, Z),30))

 */

