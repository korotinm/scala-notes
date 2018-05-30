package my.experiments.typetag


import scala.reflect.runtime.universe._


object CheckTypeTag {

  def test[T](list: T)(implicit tag: TypeTag[T]) = {
    val (p, s, a) =
      tag.tpe match {
        case TypeRef(pre, sym, args) => (pre, sym, args)
      }

    println(s"type of $list has type argument $a")
    println(s"pre=$p; sym=$s")
  }

  def main(args: Array[String]): Unit = {
    test(List(15))
    test(15.0)
  }

}

/*output:
type of List(15) has type argument List(Int)
pre=scala.collection.immutable.type; sym=class List
type of 15.0 has type argument List()
pre=scala.type; sym=class Double
*/
