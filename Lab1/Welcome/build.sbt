ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "2.0.5"
libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.5"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.11"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.7.0"


val AkkaVersion = "2.7.0"
lazy val root = (project in file("."))
  .settings(
    name := "Welcome"
  )
