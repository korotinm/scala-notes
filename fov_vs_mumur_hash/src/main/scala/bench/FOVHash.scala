package bench

object FOVHash {

  val hash: Int = 0x811c9dc5
  val prime: Int = 0x01000193

  def fnvMinus1a(str: String): Int = {
    str.foldLeft(hash) {
      case (acc, ch) => (acc ^ ch.toByte) * prime
    }
  }

  def fnvMinus1(str: String): Int = {
    str.foldLeft(hash) {
      case (acc, ch) => (acc * prime) ^ ch.toByte
    }
  }
}
