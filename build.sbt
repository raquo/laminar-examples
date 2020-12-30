enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin) // only needed for your JS dependencies, not for Laminar

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "2.13.4"

crossScalaVersions := Seq("2.12.11", "2.13.4")

libraryDependencies += "com.raquo" %%% "laminar" % "0.11.1-SNAPSHOT"

libraryDependencies += "com.raquo" %%% "airstream" % "0.11.2-SNAPSHOT"

npmDependencies in Compile += "@material/mwc-button" -> "0.18.0"

npmDependencies in Compile += "@material/mwc-linear-progress" -> "0.18.0"

npmDependencies in Compile += "@material/mwc-slider" -> "0.18.0"

scalaJSUseMainModuleInitializer := true

scalaJSLinkerConfig in (Compile, fastOptJS) ~= { _.withSourceMap(false) }

useYarn := true
