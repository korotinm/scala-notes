package my.training.cats.ch4_monad.exercise

import cats.data.State
import cats.syntax.applicative._

object StateEvaluator {

  type CalcState[A] = State[List[Int], A]

  def eval(sym: String): CalcState[Int] =
    sym match {
      case "+" => calc(_ + _)
      case "*" => calc(_ * _)
      case "/" => calc(_ / _)
      case "-" => calc(_ - _)
      case num => push(num.toInt)
    }

  def evalAll(symbols: List[String]): CalcState[Int] =
    symbols.foldLeft(0.pure[CalcState]){(acc, v) =>
      acc.flatMap(_ => eval(v))
    }

  def push(num: Int): CalcState[Int] =
    State[List[Int], Int] { stack =>
      (num :: stack, num)
    }

  def calc(func: (Int, Int) => Int): CalcState[Int] = State[List[Int], Int] {
    case a :: b :: tail =>
      val res = func(a, b)
      (res :: tail, res)
    case _ =>
      throw new RuntimeException("error")
  }

  def main(args: Array[String]): Unit = {
    val test = for {
      _ <- eval("1")
      _ <- eval("2")
      ans <- eval("+")
    } yield ans
    println("test: " + test.run(Nil).value)

    val test2 = for {
      _ <- eval("1")
      _ <- eval("2")
      _ <- eval("+")
      _ <- eval("3")
      ans <- eval("*")
    } yield ans
    println("test2: " + test2.run(Nil).value)

    println("test3: " + evalAll(List("1", "2", "+", "3", "*")).run(Nil).value)

    val test4 = for {
      _ <- evalAll(List("1", "2", "+"))
      _ <- evalAll(List("3", "4", "+"))
      ans <- evalAll(List("*"))//or eval("*")
    } yield ans

    println("test4: " + test4.run(Nil).value)

  }

}
