package my.training.cats.ch4_monad


object CatsReaderStep6 extends App {

  def composingReaders() {
    import cats.data.Reader

    println("\n---composingReaders---\n")

    val catName: Reader[Cat, String] = Reader(cat => cat.name)
    println("catName run: " + catName.run(Cat("Bob", "meat")))

    val helloKitty: Reader[Cat, String] = catName.map("Hello " + _)
    println("helloKitty run: " + helloKitty.run(Cat("Bob", "meat")))

    val feed: Reader[Cat, String] = Reader(cat => s"my favorite feed is ${cat.favoriteFood}")

    val composedReader: Reader[Cat, String] = for {
      hello <- catName.map(_ + ": ")
      feed <- feed
    } yield s"$hello $feed"

    println("composedReader: " + composedReader(Cat("Bob", "raw meat")))
  }

  composingReaders()

}

case class Cat(name: String, favoriteFood: String)

/*console output

  ---composingReaders---

catName run: Bob
helloKitty run: Hello Bob
composedReader: Bob:  my favorite feed is raw meat

 */
