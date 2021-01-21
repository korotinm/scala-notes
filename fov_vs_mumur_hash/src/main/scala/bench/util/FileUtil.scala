package bench.util

import java.io.PrintWriter
import java.io.File
import scala.util.Random
import scala.io.Source

object FileUtil {

  private def writeWords(fileName: String, words: List[String]) = {
    val path = this.getClass().getResource("/data").getPath + s"/$fileName.txt"
    val file = new File(path)

    if (file.exists())
      println(s"Deleted file: ${file.getName()} ${file.delete()}")

    println(s"Created file: ${file.getName()} ${file.createNewFile()}")
    val pw = new PrintWriter(file)
    pw.write(words.mkString("\n"))
    pw.close()
  }

  def readWords(fileName: String): List[String] = {
    val is = this.getClass().getResourceAsStream(s"/data/$fileName.txt")
    val source = Source.fromInputStream(is)
    source.getLines().toList
  }

  private def buildWords(count: Int): List[String] =
    (1 to count).map(_ => (1 to (5 + Random.nextInt(7))).map(_ => Random.nextPrintableChar()).mkString).toList

  def main(args: Array[String]): Unit = {
    val counts = List(100000, 400000, 700000, 1000000, 1300000, 1600000, 1900000, 2100000)
    counts.foreach { count =>
      writeWords(s"words_$count", buildWords(count))
    }
  }
}
