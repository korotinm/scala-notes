package my.experiments.recursion

import scala.util.control.TailCalls.{tailcall, done, TailRec}

import cats.implicits._
import cats.free.Trampoline
import cats.Eval

object MutualRecursion extends App {

  val limit = 10000000

  //warm up
  (1 to 10).foreach{_ =>
    first(0).result
    first2(0).run
    first3(0).value
  }


  //---tailrec---
  def first(v: Int): TailRec[Int] = {
    if (v >= limit)
      done(v)
    else
      tailcall(second(v))
  }

  def second(v: Int): TailRec[Int] =
    tailcall(first(v + 1))

  val timeTailRec = System.nanoTime()
  println(s"result = ${first(0).result}")
  println(s"timeTailRec = ${System.nanoTime() - timeTailRec}")


  //---cats Trampoline---
  def first2(v: Int): Trampoline[Int] =
    if (v >= limit)
      Trampoline.done(v)
    else
      Trampoline.defer(second2(v))

  def second2(v: Int): Trampoline[Int] =
    Trampoline.defer(first2(v + 1))

  val timeTrampoline = System.nanoTime()
  println(s"\nresult = ${first2(0).run}")
  println(s"timeTrampoline = ${System.nanoTime() - timeTrampoline}")


  //---cats Eval---
  def first3(v: Int): Eval[Int] =
    Eval.always(v >= limit).flatMap{
      case true => Eval.now(v)
      case false => second3(v)
    }

  def second3(v: Int): Eval[Int] =
    Eval.defer(first3(v + 1))

  val timeEval = System.nanoTime()
  println(s"\nresult = ${first3(0).value}")
  println(s"timeEval = ${System.nanoTime() - timeEval}")



}

/* 1. console output
result = 10000000
timeTailRec = 373725240

result = 10000000
timeTrampoline = 666089928

result = 10000000
timeEval = 627727014
 */

/* 2. console output
result = 10000000
timeTailRec = 158610310

result = 10000000
timeTrampoline = 355055989

result = 10000000
timeEval = 355197475
 */


/* 3. console output
result = 10000000
timeTailRec = 170295951

result = 10000000
timeTrampoline = 344628028

result = 10000000
timeEval = 361608670
 */
