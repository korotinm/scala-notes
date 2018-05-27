package my.train.stream.step1

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._


object TryStream {

  implicit val system = ActorSystem("reactive-tweets")

  implicit val materializer = ActorMaterializer()

  implicit val execContext = scala.concurrent.ExecutionContext.global

  def main(args: Array[String]): Unit = {
    val tweets: Source[String, NotUsed] =
      Source(
        "a" ::
        "b" ::
        "abc" ::
        "aad" ::
        "bbc" ::
        "bbc" ::
        Nil
      )

    val buffer = Flow[String].scan[Set[String]](Set.empty[String])((set, s) => set + s)


    tweets
      .map(_.toUpperCase)
      .via(buffer)
      .runWith(Sink.foreach(v => println(v.mkString + s": ${System.nanoTime()} nano")))
      .onComplete {
        case _ => system.terminate()
      }
  }
}
