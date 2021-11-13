package layer

import zio.console._
import zio.ExitCode
import zio.ZEnv
import zio.URIO
import zio.Task
import zio.ZLayer
import zio.Has
import zio.ZIO

object ZLayerExample extends zio.App {

  val greeting = for {
    _ <- putStrLn("What is your name")
    name <- getStrLn
    _ <- putStrLn(s"Hello $name")
  } yield ()

  case class User(name: String, email: String)

  object UserEmailer {
    type UserEmailerEnv = Has[UserEmailer.Service]

    trait Service {
      def notify(user: User, message: String): Task[Unit]
    }

    val live: ZLayer[Any, Nothing, UserEmailerEnv] = ZLayer.succeed(new Service {

      override def notify(user: User, message: String): Task[Unit] = Task {
        println(s"[User emailer] Sending '$message' to ${user.email}")
      }
    })

    def notify(user: User, message: String): ZIO[UserEmailerEnv, Throwable, Unit] =
      ZIO.accessM(hasService => hasService.get.notify(user, message))
  }

  object UserDb {
    type UserDbEnv = Has[UserDb.Service]

    trait Service {
      def insert(user: User): Task[Unit]
    }

    val live = ZLayer.succeed(new Service {

      override def insert(user: User): Task[Unit] = Task {
        println(s"[Database] insert into user values ('${user.email}')")
      }
    })

    def insert(user: User): ZIO[UserDbEnv, Throwable, Unit] =
      ZIO.accessM(hasService => hasService.get.insert(user))
  }

  import UserDb._
  import UserEmailer._

  //horizontal composition
  val backendLayer: ZLayer[Any, Nothing, UserDbEnv with UserEmailerEnv] = UserDb.live ++ UserEmailer.live

  //vertical composition
  object UserSubscription {
    type UserSubscriptionEnv = Has[UserSubscription.Service]

    class Service(notifier: UserEmailer.Service, userDb: UserDb.Service) {

      def subscribe(user: User): Task[User] =
        for {
          _ <- userDb.insert(user)
          _ <- notifier.notify(user, s"Hello, ${user.name}. You have been subscribed to the newsletter")
        } yield user
    }

    val live: ZLayer[UserEmailerEnv with UserDbEnv, Nothing, UserSubscriptionEnv] =
      ZLayer.fromServices[UserEmailer.Service, UserDb.Service, UserSubscription.Service] { (userEmailer, userDb) =>
        new Service(userEmailer, userDb)
      }

    def subscribe(user: User): ZIO[UserSubscriptionEnv, Throwable, User] =
      ZIO.accessM(_.get.subscribe(user))
  }

  import UserSubscription._
  val subscriptionLayer: ZLayer[Any, Nothing, UserSubscriptionEnv] = backendLayer >>> UserSubscription.live

  val user = User("Mikhail", "mikhail@domain.com")
  val message = "Hello, Mikhail ..."

/*   def notifyMikhail() =
    UserEmailer
      .notify(user, message)
      .provideLayer(backendLayer)
      .exitCode */

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    UserSubscription
      .subscribe(user)
      .provideLayer(subscriptionLayer)
      .exitCode
}
