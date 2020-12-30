package my.experiments.bloomfilter

/**
 *
 * Простая реализация - Bloom filter
 *
 */
class SimpleBloomFilter(size: Int, capacity: Int) {
  import scala.util.hashing.MurmurHash3
  import Math._

  require(size >= capacity)

  private val filter = new Array[Int](size)
  private val numberOfHashIteration: Int = (round((size / capacity) * log(2.0))).toInt

  private def hash(value: String, n: Int): Int = {
    def _hash(i: Int, acc: Int): Int =
      i match {
        case _ if i == n => acc
        case _ =>
          val hashRes = MurmurHash3.stringHash(value + i)
          _hash(i + 1, acc + hashRes)
      }

    abs(_hash(0, 0)) % size
  }

  def add(value: String): Unit =
    (1 to numberOfHashIteration).foreach { i =>
      filter(hash(value, i)) = 1
    }

  def has(value: String): Boolean =
    !(1 to numberOfHashIteration).exists(i => filter(hash(value, i)) == 0)
}

object SimpleBloomFilter extends App {
  // case 1
  {
    val size = 190000
    val capacity = 10000

    val bloomFilter = new SimpleBloomFilter(size, capacity)
    assert(bloomFilter.numberOfHashIteration == 13)

    (1 to capacity).foreach(i => bloomFilter.add(s"test_${i.toString()}"))
    assert((1 to capacity).map(i => bloomFilter.has(s"test_${i.toString()}")).exists(_ == false) == false)

    val seq =
      (capacity + 1 to capacity * 2)
        .filter(i => bloomFilter.has(s"test_${i.toString()}"))
    assert(seq.size == 0)
  }

  // case 2
  {
    val size = 3000000
    val capacity = 1000000

    val bloomFilter = new SimpleBloomFilter(size, capacity)
    assert(bloomFilter.numberOfHashIteration == 2)

    (1 to capacity).foreach(i => bloomFilter.add(s"test_${i.toString()}"))
    assert((1 to capacity).map(i => bloomFilter.has(s"test_${i.toString()}")).exists(_ == false) == false)
  }

  //case 3
  {
    val size = 10
    val capacity = 6

    val bloomFilter = new SimpleBloomFilter(size, capacity)
    assert(bloomFilter.numberOfHashIteration == 1)

    val list = List("qwerty", "test", "abc", "qwertyuioplkjhgfdsazxcvbnm", "1234567890", "+-)(*&^%$#@!")

    list.foreach(item => bloomFilter.add(item))

    assert(list.map(item => bloomFilter.has(item)).exists(_ == false) == false)

    assert(bloomFilter.has("qwerty") == true)
  }
}
