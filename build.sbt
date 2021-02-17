name := "restaurantStreaming"

version := "0.1"
scalaVersion := "2.13.4"
ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "com.example"
val AkkaVersion = "2.6.12"
val AkkaHttpVersion = "10.2.3"

lazy val streams = (project in file("."))
  .settings(
    resolvers += Resolver.mavenLocal,
    resolvers += Resolver.sonatypeRepo("public"),
    scalacOptions ++= Seq("-deprecation", "-Yrangepos"),
    name := "Streams",// use Scalafix compatible version
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % Test,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.lightbend.akka" %% "akka-stream-alpakka-s3" % "2.0.2",
      "com.typesafe.akka" %% "akka-http-xml" % AkkaHttpVersion,
    ),
    libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6",
    libraryDependencies += "com.typesafe" % "config" % "1.4.1",
    libraryDependencies += "com.github.kxbmap" %% "configs" % "0.5.0",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  )

