enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin) // only needed for your JS dependencies, not for Laminar

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.12.10", "2.13.1")

libraryDependencies += "com.raquo" %%% "laminar" % "0.10.2"

npmDependencies in Compile += "@material/mwc-button" -> "0.18.0"

scalaJSUseMainModuleInitializer := true

scalaJSLinkerConfig in (Compile, fastOptJS) ~= { _.withSourceMap(false) }

useYarn := true
