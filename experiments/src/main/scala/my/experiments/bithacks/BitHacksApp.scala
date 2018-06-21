package my.experiments.bithacks

object BitHacksApp {




  def main(args: Array[String]): Unit = {

    (0 to Int.MaxValue).foreach{v =>
      if(v.is2PowOf)
        println(v + " YES")
      if(v.fastIs2PowOf)
        println(v + " YES f")
    }


  }

}
