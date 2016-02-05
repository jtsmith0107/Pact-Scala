import Dependencies._
import sbt.Keys._
import play.PlayImport.PlayKeys._

organization in ThisBuild := "com.rallyhealth.play.pact"


// Cross Compile for sbt plugin support and ability to pull into up to date libs
crossScalaVersions := Seq("2.11.7")

// NOTE: rally-sbt-plugin handles a number of common settings for us, including but not limited to:
// * cross scala versions
// * the publishTo location and logic
// * the rally published artifact resolver.

// Override scalacOptions set by rally-sbt-plugin to be more strict.
scalacOptions in ThisBuild := Seq(
  "-Xfatal-warnings",
  "-feature",
  "-deprecation",
  "-unchecked"
)

lazy val commonRootSettings = Seq(
  organization := "com.rallyhealth.play.pact",
  organizationName := "Rally Health",
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
  buildInfoPackage := "pactVerifier",
  scalaVersion := "2.11.7"
)

lazy val baseSettings = Seq[SettingsDefinition](

  resolvers += Resolver.url(
    "ivy-libs-snapshot-local"
  ),

  // Some libraries use different versions of scala than we do e.g. we use 2.11.6, and others might use 2.11.4 etc.
  // This forces use of the scala version we specify, and means we don't get an evication notice.
  ivyScala := ivyScala.value map {
    _.copy(overrideScalaVersion = true)
  },

  publishArtifact := true,

  // Don't publish any test artifacts. We don't have any test code that others need
  publishArtifact in Test := false
) ++ commonRootSettings

/**
 * Base Model's project defines what a pact contract looks like and how to parse it to and from json.
 */
val models = project.in(file("models"))
  .settings(baseSettings ++: commonRootSettings: _*)
  .settings(
    name := "pact-models"
  )

/**
 * Provider defines how we would like to compare service response to a given pact file.
 */
val provider = project.in(file("provider"))
  .settings(baseSettings: _*)
  .settings(
    name := "pact-provider",
    libraryDependencies ++= Seq(
      Dependencies.scalaTest
    )
  ).dependsOn(models % "compile->compile;test->test")

val pactVerifierPlugin = project.in(file("pact-verifier-plugin"))
  .settings(baseSettings ++: commonRootSettings: _*)
  .settings(sbtPlugin := true)
  .dependsOn(models, provider)

/**
 * Project to assemble fixtures and external client code into a test dsl
 */
val scalaTestConsumer = project.in(file("consumer-scalatest"))
  .settings(baseSettings ++: commonRootSettings: _*)
  .settings(fork := false)
  .settings(
    name := "pact-jvm-consumer-scalatest",
    libraryDependencies ++= Seq(
      Dependencies.pactConsumer,
      Dependencies.scalaTest
    // Added to test against some "standard"
//      Dependencies.playJson % "test",
//      Dependencies.playWs % "test"
//      Dependencies.slf4jApi,
//      Dependencies.jline,
//      Dependencies.playWs % "test" // Only needed to test our lib
    )
  )
  .dependsOn(models)