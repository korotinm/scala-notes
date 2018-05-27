package my.training.cats.ch8_asynchronousCode

import cats.{Applicative, Id}
import cats.syntax.functor._
import cats.instances.future._
import cats.instances.list._
import cats.syntax.traverse._

import scala.language.higherKinds
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object AsynchronousCode {

  def testTotalUptime() = {
    val hosts = Map("host1" -> 10, "host2" -> 2)
    val client = new TestUptimeClientImpl(hosts)

    val service = new UptimeService(client)
    val service2 = new UptimeService2(client)

    val res = service.totalUptime(hosts.keys.toList)
    val res2 = service2.totalUptime(hosts.keys.toList)

    assert(res == hosts.values.sum)
    assert(res2 == hosts.values.sum)
  }

  def main(args: Array[String]): Unit = {
    testTotalUptime()
  }

}

trait UptimeClient[F[_]] {
  def getUptime(hostname: String): F[Int]
}

trait RealUptimeClient extends UptimeClient[Future] {
  override def getUptime(hostname: String): Future[Int]

}

trait TestUptimeClient extends UptimeClient[Id] {
  override def getUptime(hostname: String): Id[Int]
}

class UptimeService[F[_]](client: UptimeClient[F])(implicit appl: Applicative[F]) {
  def totalUptime(hostnames: List[String]): F[Int] =
    hostnames.traverse(client.getUptime).map(_.sum)
}

class UptimeService2[F[_]: Applicative](client: UptimeClient[F]) {
  def totalUptime(hostnames: List[String]): F[Int] =
    hostnames.traverse(client.getUptime).map(_.sum)
}

class TestUptimeClientImpl(hosts: Map[String, Int]) extends TestUptimeClient {
  override def getUptime(hostname: String): Id[Int] =
    hosts.getOrElse(hostname, 0)
}
