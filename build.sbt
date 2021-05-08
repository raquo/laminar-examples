enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin) // only needed for your JS dependencies, not for Laminar

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "2.13.5"

crossScalaVersions := Seq("2.12.11", "2.13.5")

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "0.13.0",
  "com.lihaoyi" %%% "upickle" % "1.3.12",
  "com.raquo" %%% "waypoint" % "0.4.0"
)

npmDependencies in Compile += "@material/mwc-button" -> "0.18.0"

npmDependencies in Compile += "@material/mwc-linear-progress" -> "0.18.0"

npmDependencies in Compile += "@material/mwc-slider" -> "0.18.0"

scalaJSUseMainModuleInitializer := true

scalaJSLinkerConfig in (Compile, fastOptJS) ~= { _.withSourceMap(false) }

scalaJSLinkerConfig in (Compile, fullOptJS) ~= { _.withSourceMap(false) }

useYarn := true
