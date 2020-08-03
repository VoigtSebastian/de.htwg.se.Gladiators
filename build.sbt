//name := "de.htwg.se.Gladiators"
import sbt.Keys.libraryDependencies

addCompilerPlugin(scalafixSemanticdb)

ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.12.12"
ThisBuild / trapExit := false
ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-encoding", "utf8", "-feature", "-Yrangepos", "-Ywarn-unused")

resolvers += Resolver.sonatypeRepo("snapshots")

val commonDependencies = Seq(
    "org.scalactic" %% "scalactic" % "3.1.1",
    "org.scalatest" %% "scalatest" % "3.1.1" % Test,
    "org.scalamock" %% "scalamock" % "4.4.0" % Test,
    "com.propensive" %% "kaleidoscope" % "0.1.0",
    "com.beachape" %% "enumeratum" % "1.6.1",
    "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
    "org.scalafx" %% "scalafx" % "14-R19"
)

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m=>
  "org.openjfx" % s"javafx-$m" % "14.0.1" classifier osName
)

libraryDependencies ++= commonDependencies
