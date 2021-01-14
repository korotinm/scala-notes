package my.experiments.fnvHash

//https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function#FNV-1a_hash
object FnvHash {

  def fnvMinus1a_32(str: String): Int = {
    val hash: Int = 0x811c9dc5
    val prime: Int = 0x01000193

    str.foldLeft(hash) {
      case (acc, ch) => (acc ^ ch) * prime
    }
  }
}
