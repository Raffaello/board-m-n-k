import sbt.Keys.{libraryDependencies, parallelExecution}

// ---- scalameter
// see: https://github.com/scalameter/scalameter-examples/blob/master/basic-with-separate-config/build.sbt
lazy val Benchmark = config("bench") extend Test
lazy val settingsScalameter = Seq(
  name in Benchmark := "board-m-n-k-benchmarks",
  publishArtifact in Benchmark := false,
  scalacOptions in Benchmark ++= Seq(
    "-optimize",
    "-opt", "_",
  ),
  parallelExecution in Benchmark := false,
  logBuffered in Benchmark := false,
  fork in Benchmark := true,
  outputStrategy in Benchmark := Some(StdoutOutput),
  connectInput in Benchmark := true,
  testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),
)
// --- end - scalameter

lazy val root = Project(
  "board-m-n-k", file("."))
  .settings(Defaults.coreDefaultSettings)
  .configs(Benchmark)
  .settings(inConfig(Benchmark)(Defaults.testSettings ++ settingsScalameter): _*
  )

name := "board-m-n-k"
version := "0.1"
scalaVersion := "2.12.8"
scalacOptions ++= Seq(
  "-Ypartial-unification",
  "-deprecation",
  "-feature",
  "-explaintypes",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Ywarn-unused", "_",
  "-Xlint", "_",
  //  "-Xdisable-assertions",
)

scalacOptions in Test ++= Seq(
  "-Xdev"
)

javacOptions += "--illegal-access=warn"

resolvers in Benchmark ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies ++= Seq(
  "com.storm-enroute" %% "scalameter" % "0.17" % "bench",
  "com.storm-enroute" %% "scalameter-core" % "0.17" % "bench"
)

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0-M1"

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