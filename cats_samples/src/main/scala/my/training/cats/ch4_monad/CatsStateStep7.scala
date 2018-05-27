package my.training.cats.ch4_monad

object CatsStateStep7 extends App {

  def compositingAndTransform() {
    import cats.data.State

    val state1 = State[Int, String] {num =>
      val ans = num + 1
      (ans, s"Result of state1: $ans")
    }

    val state2 = State[Int, String] {num =>
      val ans = num * 2
      (ans, s"Result of state2: $ans")
    }

    val composedState = for {
      a <- state1
      b <- state2
    } yield (a, b)

    val (state, value) =  composedState.run(15).value

    println("state: " + state)
    println("value: " + value)
  }
  compositingAndTransform()

}
