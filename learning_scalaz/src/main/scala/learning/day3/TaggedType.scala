package learning.day3

object TaggedType {
  type Tagged[U] = { type Tag = U }
  type @@[T, U] = T with Tagged[U]
}

object Example extends App {
  import TaggedType._

  sealed trait Day
  type Daytime = Long @@ Day

  final case class DaytimeRange(from: Daytime, to: Daytime)

  def Daytime(i: java.lang.Long): Daytime = i.asInstanceOf[Daytime]

  def makeRange(from: Daytime, to: Daytime): DaytimeRange = DaytimeRange(from, to)

  {
    val day: Daytime = 10L.asInstanceOf[Daytime]
    println(s"day = $day")

    val day2: Daytime = Daytime(10L)
    println(s"day2 = $day2")

    val dtRange = makeRange(Daytime(1L), Daytime(10L))
    println(s"dtRange = $dtRange")
  }
}
