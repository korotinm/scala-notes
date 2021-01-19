package bench

import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import scala.util.Random

/* object PrintableWord {
  lazy val words_100000 = buildWords(100000)
  lazy val words_400000 = buildWords(400000)
  lazy val words_700000 = buildWords(700000)
  lazy val words_1000000 = buildWords(1000000)
  lazy val words_1300000 = buildWords(1300000)
  lazy val words_1600000 = buildWords(1600000)
  lazy val words_1900000 = buildWords(1900000)
  lazy val words_2100000 = buildWords(2100000)

  private def buildWords(count: Int): List[String] =
    (1 to count).map(_ => (1 to (5 + Random.nextInt(6))).map(_ => Random.nextPrintableChar()).mkString).toList
} */

@State(Scope.Benchmark)
class PrintableWordState {

  lazy val words_100000 = buildWords(100000)
  lazy val words_400000 = buildWords(400000)
  lazy val words_700000 = buildWords(700000)
  lazy val words_1000000 = buildWords(1000000)
  lazy val words_1300000 = buildWords(1300000)
  lazy val words_1600000 = buildWords(1600000)
  lazy val words_1900000 = buildWords(1900000)
  lazy val words_2100000 = buildWords(2100000) 

  protected def buildWords(count: Int): List[String] =
    (1 to count).map(_ => (1 to (5 + Random.nextInt(6))).map(_ => Random.nextPrintableChar()).mkString).toList
}

/* class PrintableWordState_100000 extends PrintableWordState {
  val words = buildWords(100000)
}

class PrintableWordState_400000 extends PrintableWordState {
  val words = buildWords(400000)
}

class PrintableWordState_700000 extends PrintableWordState {
  val words = buildWords(700000)
}

class PrintableWordState_1000000 extends PrintableWordState {
  val words = buildWords(1000000)
}

class PrintableWordState_1300000 extends PrintableWordState {
  val words = buildWords(1300000)
}

class PrintableWordState_1600000 extends PrintableWordState {
  val words = buildWords(1600000)
}

class PrintableWordState_1900000 extends PrintableWordState {
  val words = buildWords(1900000)
}

class PrintableWordState_2100000 extends PrintableWordState {
  val words = buildWords(2100000) 
}*/
