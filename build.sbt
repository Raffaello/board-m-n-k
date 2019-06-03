import sbt.Keys.{libraryDependencies, parallelExecution}

// ---- scalameter
// see: https://github.com/scalameter/scalameter-examples/blob/master/basic-with-separate-config/build.sbt
lazy val Benchmark = config("bench") extend Test
lazy val settingsScalameter = Seq(
  name in Benchmark := "board-m-n-k-bench.benchmarks",
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

val scalaMeterVersion = "0.18"
libraryDependencies ++= Seq(
  "com.storm-enroute" %% "scalameter" % scalaMeterVersion % "bench",
  "com.storm-enroute" %% "scalameter-core" % scalaMeterVersion % "bench"
)
// --- end - scalameter

// CATS
scalacOptions ++= Seq(
  "-Ypartial-unification"
)

val catsVersion = "2.0.0-M1"
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-kernel" % catsVersion,
  "org.typelevel" %% "cats-macros" % catsVersion,
)
// CATS -- end

// Monocle
//val monocleVersion = "1.5.0" // 1.5.0-cats based on cats 1.0.x
//
//libraryDependencies ++= Seq(
//  "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
//  "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
//  "com.github.julien-truffaut" %%  "monocle-law"   % monocleVersion % "test"
//)
// Monocle --- end

// Deep learning
//val dl4jVersion = "1.0.0-beta4"
//libraryDependencies ++= Seq(
//  "org.deeplearning4j" % "deeplearning4j-core" % dl4jVersion,
//  "org.nd4j" % "nd4j-native-platform" % dl4jVersion,
//  // @see https://deeplearning4j.org/docs/latest/deeplearning4j-config-cudnn
//  "org.nd4j" % "nd4j-cuda-10.1-platform" % dl4jVersion,
//  "org.deeplearning4j" % "deeplearning4j-cuda-10.0" % dl4jVersion
//)
// Deep Learning -- -end

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
  "-deprecation",
  "-feature",
  "-explaintypes",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Ywarn-unused", "_",
  "-Xlint", "_",
  //  "-Xdisable-assertions",
)

javacOptions += "--illegal-access=warn"

resolvers in Benchmark ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
)

// Tests
scalacOptions in Test ++= Seq(
  "-Xdev"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"

logBuffered in Test := false
//parallelExecution in Test := false
// end - Tests
libraryDependencies += "com.typesafe" % "config" % "1.3.2"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

fork in run := true
//javaOptions in run += "-agentlib:hprof=cpu=samples"