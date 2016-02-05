import sbt._

object Dependencies {

  val playJson = "com.typesafe.play" %% "play-json" % "2.3.9"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.2.5"
  val mockWs = "de.leanovate.play-mockws" %% "play-mockws" % "2.3.0"
  val pactConsumer = "au.com.dius" %% "pact-jvm-consumer" % "3.2.2"
  val playWs = "com.typesafe.play" %% "play-ws" % "2.3.8"
  val slf4jApi = "org.slf4j" %% "slf4j-api" % "1.7.13"
  val dispatch = "net.databinder.dispatch" %% "dispatch-core" % "0.11.2"
}
