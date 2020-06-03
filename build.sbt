//name := "de.htwg.se.Gladiators"

import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "2.12.8"

ThisBuild / trapExit := false

val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.1.1",
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test,
  "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "com.google.inject" % "guice" % "4.2.2",
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "com.typesafe.akka" %% "akka-http"   % "10.1.12",
  "com.typesafe.akka" %% "akka-stream" % "2.6.5",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.32.0"
)

lazy val commonSettings = Seq(
  test in assembly := {}
)

lazy val root = (project in file(".")).settings(
  name := "Gladiators",
  libraryDependencies ++= commonDependencies,
  commonSettings,
  assemblyMergeStrategy in assembly := {
    case PathList("reference.conf") => MergeStrategy.concat
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  },
  assemblyJarName in assembly := "Gladiators.jar",
  mainClass in assembly := Some("de.htwg.se.gladiators.Gladiators")
).aggregate(Gladiator, Player).dependsOn(Gladiator, Player)


lazy val Gladiator = project.settings(
  name :=  "Gladiator",
  libraryDependencies ++= commonDependencies,
  commonSettings,
  assemblyMergeStrategy in assembly := {
    case PathList("reference.conf") => MergeStrategy.concat
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  },
  assemblyJarName in assembly := "Gladiator-Service.jar",
  mainClass in assembly := Some("de.htwg.se.gladiators.Gladiator")
).dependsOn(Player)

lazy val Player = project.settings(
  name :=  "PlayerMod",
  libraryDependencies ++= commonDependencies,
  commonSettings,
  assemblyMergeStrategy in assembly := {
    case PathList("reference.conf") => MergeStrategy.concat
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  },
  assemblyJarName in assembly := "Player-Service.jar",
  mainClass in assembly := Some("de.htwg.se.gladiators.playerModule.PlayerMod")
)
