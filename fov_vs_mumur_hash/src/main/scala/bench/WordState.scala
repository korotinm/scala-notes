package bench

import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import scala.util.Random

@State(Scope.Benchmark)
class WordState {
  lazy val words_100000 = buildWords(100000)
  lazy val words_400000 = buildWords(400000)
  lazy val words_700000 = buildWords(700000)
  lazy val words_1000000 = buildWords(1000000)
  lazy val words_1300000 = buildWords(1300000)
  lazy val words_1600000 = buildWords(1600000)
  lazy val words_1900000 = buildWords(1900000)
  lazy val words_2100000 = buildWords(2100000)

  private def buildWords(count: Int): List[String] =
    (1 to count).map(_ => (1 to (5 + Random.nextInt(6))).map(_ => Random.nextInt().toChar).mkString).toList
}
