enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin) // only needed for your JS dependencies, not for Laminar

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "2.13.6"

crossScalaVersions := Seq("2.12.13", "2.13.6", "3.0.2")

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "0.14.0",
  "com.lihaoyi" %%% "upickle" % "1.4.2",
  "com.raquo" %%% "waypoint" % "0.5.0"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:existentials",
)

Compile / npmDependencies += "@material/mwc-button" -> "0.18.0"

Compile / npmDependencies += "@material/mwc-linear-progress" -> "0.18.0"

Compile / npmDependencies += "@material/mwc-slider" -> "0.18.0"

scalaJSUseMainModuleInitializer := true

Compile / fastOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) }

Compile / fullOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) }

useYarn := true
