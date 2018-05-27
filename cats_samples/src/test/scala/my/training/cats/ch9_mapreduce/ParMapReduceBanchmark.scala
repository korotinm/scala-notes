package my.training.cats.ch9_mapreduce

import org.scalameter.api._
import cats.instances.string._

class ParMapReduceBanchmark
  extends Bench.LocalTime {

  val funcConcat = (v: Int) => v.toString

  val sizes = Gen.range("size")(1000, 5000, 1000)

  val ranges = for {
    size <- sizes
  } yield 0 until size

  performance of "int to string ParMapReduce" in {
    measure method "foldMap" in {
      using(ranges) in {
        r => ParMapReduce.foldMap(r.toVector)(funcConcat)
      }
    }

    measure method "parFoldMap" in {
      using(ranges) in {
        r => ParMapReduce.parFoldMap(r.toVector)(funcConcat)
      }
    }
  }

}
