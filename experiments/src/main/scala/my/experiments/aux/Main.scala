package my.experiments.aux

object Main extends App{

  import Foo._

  trait Foo[A] {
    type B
    def value: B
  }


  trait Converter[A] {
    type B
    def convert(v: A): B
  }

  object Foo {
    type Aux[A0, B0] = Foo[A0] {type B = B0}
    type AuxConverter[A0, B0] = Converter[A0] {type B = B0}

    implicit def fi = new Foo[Int] {
      type B = String
      def value = "int"
    }

    implicit def fl = new Foo[Long] {
      type B = String
      def value = "long"
    }

    implicit def fs = new Foo[String] {
      type B = Boolean
      def value = true
    }

    // converter
    implicit def  int2string = new Converter[Int] {
      type B = String
      def convert(v: Int): B = v.toString()
    }

    implicit def  int2boolean = new Converter[Int] {
      type B = Boolean
      def convert(v: Int): B = if(v <= 0) false else true
    }
  }

  def test[T, R](t: T)(implicit aux: Aux[T, R]): R = aux.value

  def convert[T, R](t: T)(implicit converter: AuxConverter[T, R]): R = converter.convert(t)

  val res = test(10L)
  println(s"res: ${res}")

  val res2 = test("string")
  println(s"res2: ${res2}")

  // test converter
  val converterRes: String = convert[Int, String](10)
  println(s"converterRes: $converterRes")
  val converterRes2: Boolean = convert[Int, Boolean](10)
  println(s"converterRes2: $converterRes2")
  
}

/* output:
res: long
res2: true
converterRes: 10
converterRes2: true
*/