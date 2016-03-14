name := "smiley"

version := "1.0.%s".format(Option(System.getenv("BUILD_NUMBER")).getOrElse("0-SNAPSHOT"))

val playTuckerVersion = "2.3.43"

val tucker = Seq(
  "com.timgroup"               % "Tucker"                % "1.0.415"
)

val tuckerPlay = Seq(
  "com.timgroup"               %% "playtuckerjvmmetrics" % playTuckerVersion,
  "com.timgroup"               %% "playtuckercore"       % playTuckerVersion)

libraryDependencies ++= Seq(
  anorm,
  cache,
  jdbc,
  ws
) ++ tucker ++ tuckerPlay

com.timgroup.sbtjavaversion.SbtJavaVersionKeys.javaVersion := "1.6"
scalaVersion := "2.11.8"

UploadToProductstore.uploadToProductstoreSettings

// sbt-assembly config per Typesafe support
mainClass in assembly := Some("com.timgroup.playlauncher.Launcher")
assemblyJarName := "smiley-" + version.value + ".jar"

// fullClasspath in assembly += Attributed.blank(playPackageAssets.value),

libraryDependencies ~= { _ map {
  case m if m.organization == "com.typesafe.play" =>
    m.exclude("commons-logging", "commons-logging")
  case m => m
}}


excludeDependencies += "commons-logging"

assemblyMergeStrategy in assembly := {
  case "play/core/server/ServerWithStop.class" => MergeStrategy.first
  case "org/apache/commons/logging/Log.class" => MergeStrategy.first
  case "logback.xml" => MergeStrategy.first
  case other => (assemblyMergeStrategy in assembly).value(other)
}

com.timgroup.sbtplaylauncher.SbtPlayLauncher.playLauncherSettings

enablePlugins(PlayScala)
