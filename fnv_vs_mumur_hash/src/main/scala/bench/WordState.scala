package bench

import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import scala.util.Random
import bench.util.FileUtil

@State(Scope.Benchmark)
class WordState {
  lazy val words_100000 = FileUtil.readWords("words_100000")
  lazy val words_400000 = FileUtil.readWords("words_400000")
  lazy val words_700000 = FileUtil.readWords("words_700000")
  lazy val words_1000000 = FileUtil.readWords("words_1000000")
  lazy val words_1300000 = FileUtil.readWords("words_1300000")
  lazy val words_1600000 = FileUtil.readWords("words_1600000")
  lazy val words_1900000 = FileUtil.readWords("words_1900000")
  lazy val words_2100000 = FileUtil.readWords("words_2100000")

  protected def buildWords(count: Int): List[String] =
    (1 to count).map(_ => (1 to (5 + Random.nextInt(6))).map(_ => Random.nextPrintableChar()).mkString).toList
}
