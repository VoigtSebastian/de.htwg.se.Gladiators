//name := "de.htwg.se.Gladiators"
import sbt.Keys.libraryDependencies

addCompilerPlugin(scalafixSemanticdb)

coverageExcludedPackages := "<empty>;de.htwg.se.gladiators.Gladiators;de.htwg.se.gladiators.aview.Gui.*"

version := "0.1"
scalaVersion := "2.12.12"
trapExit := false
scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-Yrangepos",
    "-Ywarn-unused",
    "evicted")

val commonDependencies = Seq(
    "org.scalactic" %% "scalactic" % "3.1.1",
    "org.scalatest" %% "scalatest" % "3.1.1" % Test,
    "org.scalamock" %% "scalamock" % "4.4.0" % Test,
    "com.propensive" %% "kaleidoscope" % "0.1.0",
    "com.beachape" %% "enumeratum" % "1.6.1",
    "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
    "com.softwaremill.macwire" %% "macros" % "2.3.6" % "provided",
    "com.softwaremill.macwire" %% "util" % "2.3.6"
)

val webDependencies = Seq(
    "com.typesafe.akka" %% "akka-stream" % "2.6.8",
    "com.typesafe.akka" %% "akka-http" % "10.2.0",
    "de.heikoseeberger" %% "akka-http-play-json" % "1.32.0",
    "com.typesafe.akka" %% "akka-actor-typed" % "2.6.8",
    "io.spray" %% "spray-json" % "1.3.5",
    "io.spray" %% "spray-json" % "1.3.5",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.0",
    "com.typesafe.play" %% "play-json" % "2.8.1",
)

lazy val root = (project in file(".")).settings(
    name := "Gladiators",
    libraryDependencies ++= commonDependencies,
    assemblyMergeStrategy in assembly := {
        case PathList("reference.conf") => MergeStrategy.concat
        case PathList("META-INF", xs @ _*) => MergeStrategy.discard
        case x => MergeStrategy.first
    },
    assemblyJarName in assembly := "Gladiators.jar",
    mainClass in assembly := Some("de.htwg.se.gladiators.Gladiators")
).aggregate(PlayerService).dependsOn(PlayerService)

lazy val PlayerService = project.settings(
    name := "PlayerService",
    libraryDependencies ++= commonDependencies,
    libraryDependencies ++= webDependencies,
    assemblyMergeStrategy in assembly := {
        case PathList("reference.conf") => MergeStrategy.concat
        case PathList("META-INF", xs @ _*) => MergeStrategy.discard
        case x => MergeStrategy.first
    },
    assemblyJarName in assembly := "PlayerService.jar",
    mainClass in assembly := Some("de.htwg.se.gladiators.playerService.PlayerService")
)
