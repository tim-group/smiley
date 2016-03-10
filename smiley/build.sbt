name := "smiley"

version := "1.0-SNAPSHOT"

val playTuckerVersion = "2.2.14"

val tucker = Seq(
  "com.timgroup"               % "Tucker"                % "1.0.415"
)

val tuckerPlay = Seq(
  "com.timgroup"               %% "play-jvmmetrics-tucker" % playTuckerVersion,
  "com.timgroup"               %% "play-tucker"       % playTuckerVersion)

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
) ++ tucker ++ tuckerPlay

com.timgroup.sbtjavaversion.SbtJavaVersionKeys.javaVersion := "1.6"
scalaVersion := "2.10.3"

play.Project.playScalaSettings

UploadToProductstore.uploadToProductstoreSettings

// sbt-assembly config per Typesafe support
mainClass in assembly := Some("com.timgroup.playlauncher.Launcher")

// fullClasspath in assembly += Attributed.blank(playPackageAssets.value),

libraryDependencies ~= { _ map {
  case m if m.organization == "com.typesafe.play" =>
    m.exclude("commons-logging", "commons-logging")
  case m => m
}}


// split out the settings which apply to non-play projects
excludeDependencies += "commons-logging"

assemblyMergeStrategy in assembly := {
  case "play/core/server/ServerWithStop.class" => MergeStrategy.first
  case "org/apache/commons/logging/Log.class" => MergeStrategy.first
  case "logback.xml" => MergeStrategy.first
  case other => (assemblyMergeStrategy in assembly).value(other)
}

com.timgroup.sbtplaylauncher.SbtPlayLauncher.playLauncherSettings