//name := "de.htwg.se.Gladiators"
import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / trapExit := false

val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.1.1",
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test,
  "com.propensive" %% "kaleidoscope" % "0.1.0",
  "com.beachape" %% "enumeratum" % "1.6.1"
)

// Show deprecation and feature warning
scalacOptions ++= Seq("-deprecation", "-feature")
coverageExcludedPackages := "CellPanel.scala;SwingGui.scala;Tui.scala"

libraryDependencies ++= commonDependencies
