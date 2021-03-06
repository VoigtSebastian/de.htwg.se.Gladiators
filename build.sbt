//name := "de.htwg.se.Gladiators"
import sbt.Keys.libraryDependencies

coverageExcludedPackages := "<empty>;de.htwg.se.gladiators.Gladiators;de.htwg.se.gladiators.aview.Gui.*"

version := "0.1"
scalaVersion := "2.12.12"
trapExit := false
scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-Yrangepos",
    "-Ywarn-unused")

val commonDependencies = Seq(
    "org.scalactic" %% "scalactic" % "3.1.1",
    "org.scalatest" %% "scalatest" % "3.1.1" % Test,
    "org.scalamock" %% "scalamock" % "4.4.0" % Test,
    "com.propensive" %% "kaleidoscope" % "0.1.0",
    "com.beachape" %% "enumeratum" % "1.6.1",
    "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
    "com.softwaremill.macwire" %% "macros" % "2.3.6" % "provided",
    "com.softwaremill.macwire" %% "util" % "2.3.6",
    "com.typesafe.play" %% "play-json" % "2.9.1"
)

lazy val gladiatorsBase = (project in file(".")).settings(
    name := "Gladiators",
    libraryDependencies ++= commonDependencies,
    assemblyMergeStrategy in assembly := {
        case PathList("reference.conf") => MergeStrategy.concat
        case PathList("META-INF", xs @ _*) => MergeStrategy.discard
        case x => MergeStrategy.first
    },
    assemblyJarName in assembly := "Gladiators.jar",
    mainClass in assembly := Some("de.htwg.se.gladiators.Gladiators")
)
