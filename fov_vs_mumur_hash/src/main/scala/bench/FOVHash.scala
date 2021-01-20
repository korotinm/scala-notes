package bench

object FOVHash {
  val hash: Int = 0x811c9dc5
  val prime: Int = 0x01000193

  def fnvMinus1a(str: String): Int =
    /* var acc = hash
    for (i <- 0 until str.length()) {
      acc = acc ^ str.charAt(i).toByte
      acc = acc * prime
    }

    acc */

    str.foldLeft(hash) {
      case (acc, ch) => (acc ^ ch.toByte) * prime
    }

  def fnvMinus1(str: String): Int =
    /* var acc = hash
    for (i <- 0 until str.length()) {
      acc = acc * prime
      acc = acc ^ str.charAt(i).toByte
    }

    acc */

    str.foldLeft(hash) {
      case (acc, ch) => (acc * prime) ^ ch.toByte
    }
}
