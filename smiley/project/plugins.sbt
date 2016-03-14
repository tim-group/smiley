// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.timgroup" % "sbt-java-version" % "0.0.18")

addSbtPlugin("com.timgroup" % "upload-to-productstore" % "1.1.14")

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.0")

addSbtPlugin("com.timgroup" % "sbt-play-launcher" % "0.0.6")