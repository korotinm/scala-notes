package my.experiments.syntax

import my.experiments.syntax.Enrich

object EnrichStep1 extends App {

  def enrichTuple2 {

    implicit def enrich[A](a: A) = new Enrich[A](a)

    implicit def enrichTuple2[T1, T2](tpl: (T1, T2)) = new EnrichTuple2[T1, T2](tpl)

    println("\n---enrichTuple2---\n")

    val tuple2Int = 5.tuple2(10)
    println("tuple2Int: " + tuple2Int)

    val tpl3Person = tuple2Int.append(Person("Mike", 30))
    println("tpl3Person: " + tpl3Person)

    val tpl3 = tuple2Int.append("item")
    println("tpl3: " + tpl3)

    val tuple2Bool = true.tuple2(false)
    println("tuple2Bool: " + tuple2Bool)
  }

  enrichTuple2

}

final class Enrich[A](a: A) {
  def tuple2(b: A): (A, A) = (a, b)
}

final class EnrichTuple2[T1, T2](tpl: (T1, T2)) {
  def append[P](v: P): (T1, T2, P) = (tpl._1, tpl._2, v)
}


case class Person(name: String, age: Int)

/* console output:

---enrichTuple2---

tuple2Int: (5,10)
tpl3Person: (5,10,Person(Mike,30))
tpl3: (5,10,item)
tuple2Bool: (true,false)

 */