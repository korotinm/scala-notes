package my.experiments

import scala.annotation.tailrec

package object bithacks {

  private val long2PowOfSet = {
    (0 to 62).map(pow =>
      Math.pow(2, pow).toLong
    ).toSet
  }

  implicit class BitHacksInt(v: Int) {

    def fastIs2PowOf: Boolean = long2PowOfSet.contains(v)

    //it's slower than the method fastIs2PowOf
    def is2PowOf: Boolean = {
      @tailrec
      def checkIt(n: Int, acc: Int): Boolean =
        if (acc > 1) false
        else if (n == 0) acc == 1
        else checkIt(n & (n - 1), acc + 1)


      checkIt(v, 0)
    }
  }

  implicit class BitHacksLong(v: Long) {

    def is2PowOf: Boolean = long2PowOfSet.contains(v)

  }

}
