name := "smiley"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)

com.timgroup.sbtjavaversion.SbtJavaVersionKeys.javaVersion := "1.8"

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