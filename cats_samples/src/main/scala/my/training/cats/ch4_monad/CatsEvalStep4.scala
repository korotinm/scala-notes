package my.training.cats.ch4_monad

import cats.Eval

import scala.annotation.tailrec
import scala.util.Random

object CatsEvalStep4 {

  def factorial(v: BigInt): Eval[BigInt] = {
    if(v == 1)
      Eval.now(1)
    else
      Eval.defer(factorial(v - 1).map(_ * v))
  }

  @tailrec
  def factorial2(v: BigInt, acc: BigInt = 1): BigInt = {
    if(v == 1)
      acc
    else
      factorial2(v - 1, acc * v)
  }

  def main(args: Array[String]): Unit = {
    val evalChain = Eval.always{
      println("Step 1:")
      "Be"

    }.map{s =>
      println("Step 2:")
      s"$s careful"

    }.memoize.map{s =>
      println("Step 3:")
      s"$s with using Eval chain"
    }

    println(evalChain.value)
    println(evalChain.value)

    val evalForComprehension = for{
      a <- Eval.now(100)
      b <- Eval.always(Random.nextGaussian())
    } yield {
      println("Result:")
      a * b
    }

    println(evalForComprehension.value)
    println(evalForComprehension.value)

    //factorial with using defer
    println("\nFactorial 20000: ")
    var time = System.currentTimeMillis()
    println(factorial(20000).value)
    val res = System.currentTimeMillis() - time

    println("\nFactorial2 20000: ")
    time = System.currentTimeMillis()
    println(factorial2(20000))
    val res2 = System.currentTimeMillis() - time

    println(s"\ntime: res=$res; res2=$res2")
    println("tailrec is faster approach than Eval.defer")
  }
}
