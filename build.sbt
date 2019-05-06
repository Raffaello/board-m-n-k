name := "tic-tac-toe"

version := "0.1"

scalaVersion := "2.12.7"

scalacOptions += "-Ypartial-unification"
//scalacOptions += "-Xdisable-assertions"

javacOptions += "--illegal-access=warn"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.2"

//libraryDependencies += "org.typelevel" %% "cats-core" % "1.5.0-RC1"
//val monocleVersion = "1.5.0" // 1.5.0-cats based on cats 1.0.x
//
//libraryDependencies ++= Seq(
//  "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
//  "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
//  "com.github.julien-truffaut" %%  "monocle-law"   % monocleVersion % "test"
//)

//fork in run := true
//javaOptions in run += "-agentlib:hprof=cpu=samples"