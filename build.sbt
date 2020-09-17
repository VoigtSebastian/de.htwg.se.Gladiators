//name := "de.htwg.se.Gladiators"
import sbt.Keys.libraryDependencies

addCompilerPlugin(scalafixSemanticdb)

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

libraryDependencies ++= commonDependencies
