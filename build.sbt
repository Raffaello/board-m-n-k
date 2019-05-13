//lazy val Benchmark = config("bench") extend Test
//lazy val root = (project in file("."))
//    .configs(Benchmark)
//    .settings(
//      inConfig(Benchmark)(Defaults.testSettings): _*
//    )

name := "board-m-n-k"
version := "0.1"
scalaVersion := "2.12.8"
scalacOptions += "-Ypartial-unification"
//scalacOptions += "-Xdisable-assertions"
javacOptions += "--illegal-access=warn"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

// ---- scalameter
// see: https://github.com/scalameter/scalameter-examples/blob/master/basic-with-separate-config/build.sbt
//resolvers ++= Seq(
//  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
//  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
//)

//libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.17" % "bench"
//libraryDependencies += "com.storm-enroute" %% "scalameter-core" % "0.17" % "bench"
//testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")
//parallelExecution in Benchmark := false
//logBuffered := false
//fork := true
//outputStrategy := Some(StdoutOutput)
//connectInput := true
// --- end - scalameter

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