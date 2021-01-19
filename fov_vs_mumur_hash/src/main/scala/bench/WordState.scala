package bench

import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import scala.util.Random

@State(Scope.Benchmark)
abstract class WordState {

  protected def buildWords(count: Int): List[String] =
    (1 to count).map(_ => (1 to (5 + Random.nextInt(6))).map(_ => Random.nextInt().toChar).mkString).toList
}

class WordState_100000 extends WordState {
  val words = buildWords(100000)
}

class WordState_400000 extends WordState {
  val words = buildWords(400000)
}

class WordState_700000 extends WordState {
  val words = buildWords(700000)
}

class WordState_1000000 extends WordState {
  val words = buildWords(1000000)
}

class WordState_1300000 extends WordState {
  val words = buildWords(1300000)
}

class WordState_1600000 extends WordState {
  val words = buildWords(1600000)
}

class WordState_1900000 extends WordState {
  val words = buildWords(1900000)
}

class WordState_2100000 extends WordState {
  val words = buildWords(2100000)
}
