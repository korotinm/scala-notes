import sbt.Keys._

lazy val slf4jVersion = "1.7.25"
scalaVersion in ThisBuild := "2.12.12"

//resolvers += "Akka Snapshots" at "https://repo.akka.io/snapshots/"
//resolvers += "Typesafe" at "https://repo.typesafe.com/typesafe/releases/"

updateOptions := updateOptions.value.withGigahorse(false)

lazy val catsDependencies = Seq(
  "org.typelevel" %% "cats-core" % "1.1.0",
  "org.typelevel" %% "cats-free" % "1.1.0"
)

lazy val commonDependencies = Seq(
  //"com.google.guava" % "guava" % "19.0",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0"
)

lazy val commonSettings = Seq(
  organization := "training",
  version := "0.3-SNAPSHOT",

  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-W", "30", "30"),


  javaOptions in(Test, run) += "-Xmx1G -Xms1G",
  scalacOptions ++= List(
    "-encoding", "UTF-8",
    "-target:jvm-1.8",
    "-unchecked",
    "-Ywarn-unused",
    "-deprecation",
    "-Ypartial-unification"
  ),
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  resolvers += "Typesafe" at "https://repo.typesafe.com/typesafe/releases/",

  libraryDependencies ++= commonDependencies
)

/* val common = (project in file("common"))
  .settings(commonSettings) */

lazy val cats_samples = (project in file("cats_samples"))
  .settings(commonSettings)
  .settings(
    libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.8.2" % "test",
    libraryDependencies += "com.storm-enroute" %% "scalameter-core" % "0.8.2" % "test",
    libraryDependencies ++= catsDependencies,

    //testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),
    parallelExecution in Test := false

  )

lazy val learning_scalaz = (project in file("learning_scalaz"))
  .settings(commonSettings)
  .settings(
    parallelExecution in Test := false
  )

lazy val experiments = (project in file("experiments"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= catsDependencies
  )

lazy val akka_samples = (project in file("akka_samples"))
  .settings(commonSettings)
  .settings(
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.12"
  )

  lazy val akka_typed_samples = (project in file("akka_typed_samples"))
  .settings(commonSettings)
  .settings(
    libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.5.21"
  )

lazy val akka_stream_samples = (project in file("akka_stream_samples"))
  .settings(commonSettings)
  .settings(
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.12",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.12",
    libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.1"
  )

  lazy val fnv_vs_mumur_hash = (project in file("fnv_vs_mumur_hash"))
    //.settings(commonSettings)
    .enablePlugins(JmhPlugin)


val training = (project in file("."))
  .settings(commonSettings)
  .aggregate(cats_samples)
  .aggregate(learning_scalaz)
  .aggregate(akka_samples)
  .aggregate(akka_typed_samples)
  .aggregate(akka_stream_samples)
  .aggregate(experiments)
  .aggregate(fnv_vs_mumur_hash)
  