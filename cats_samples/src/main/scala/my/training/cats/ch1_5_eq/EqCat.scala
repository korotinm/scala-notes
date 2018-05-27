package my.training.cats.ch1_5_eq

import cats.kernel.Eq
import cats.syntax.eq._
import my.training.cats.Cat
import cats.instances.string._
import cats.instances.int._

object EqCat {

  implicit val eqCat: Eq[Cat] =
    Eq.instance[Cat] { (cat1, cat2) =>
      (cat1.name === cat2.name) &&
      (cat1.age === cat2.age) &&
      (cat1.color === cat2.color)
    }

  def main(args: Array[String]): Unit = {
    val cat_1 = Cat("anton", 13, "light blue")
    val cat_2 = Cat("anton", 14, "blue")
    val cat_3 = cat_2

    println("if cat_1 === cat_2: " + (cat_1 === cat_2))
    println("if cat_2 === cat_3: " + (cat_2 === cat_3))
    println("if cat_1 =!= cat_2: " + (cat_1 =!= cat_2))

    //option cats
    val catOpt_1: Option[Cat] = Some(cat_1)
    val catOpt_2: Option[Cat] = None
    val catOpt_3: Option[Cat] = Some(cat_1)

    import cats.instances.option._
    println("if catOpt_1 === catOpt_2: " + (catOpt_1 === catOpt_2))
    println("if catOpt_1 =!= catOpt_2: " + (catOpt_1 =!= catOpt_2))
    println("if catOpt_1 === catOpt_3: " + (catOpt_1 === catOpt_3))

    val optionCat1 = Option(cat_1)
    val optionCat2 = Option.empty[Cat]

    println("if Option(cat_1) === Option.empty[Cat]: " + (optionCat1 === optionCat2))
    println("if Option(cat_1) =!= Option.empty[Cat]: " + (optionCat1 =!= optionCat2))
  }
}
