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
)

lazy val root = (project in file(".")).settings(
  name := "Gladiators",
  libraryDependencies ++= commonDependencies,
).aggregate(Shop, Gladiator, GladiatorType).dependsOn(Shop, Gladiator, GladiatorType) //% "compile->compile;test->test")

lazy val Shop = project.settings(
  name := "de.htwg.se.gladiators.model.Shop",
  libraryDependencies ++= commonDependencies,
).dependsOn(GladiatorType, Gladiator)

lazy val Gladiator = project.settings(
  name :=  "Gladiator",
  libraryDependencies ++= commonDependencies
)

lazy val GladiatorType = project.settings(
  name :=  "GladiatorType",
  libraryDependencies ++= commonDependencies
)