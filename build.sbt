name := "tic-tac-toe"

version := "0.1"

scalaVersion := "2.12.7"

scalacOptions += "-Ypartial-unification"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "org.typelevel" %% "cats-core" % "1.5.0-RC1"
