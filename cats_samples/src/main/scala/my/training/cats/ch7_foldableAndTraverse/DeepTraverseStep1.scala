package my.training.cats.ch7_foldableAndTraverse

import cats.Foldable
import cats.instances.vector._
import cats.instances.list._
import cats.instances.int._

object DeepTraverseStep1 {

  def main(args: Array[String]): Unit = {
    val deepLevel2 = List(Vector(1, 2 ,3), Vector(4, 5))
    val res = (Foldable[List] compose Foldable[Vector]).combineAll(deepLevel2)

    println("res: " + res)

    val deepLevel3 = List(List(Vector(1, 2), Vector(3)), List(Vector(1, 4), Vector(2, 2)))
    val res2 = (Foldable[List] compose Foldable[List] compose Foldable[Vector]).combineAll(deepLevel3)

    println("res2: " + res2)
  }

}

/*console output:

res: 15
res2: 15

 */
