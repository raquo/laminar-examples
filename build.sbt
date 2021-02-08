enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin) // only needed for your JS dependencies, not for Laminar

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "2.13.4"

crossScalaVersions := Seq("2.12.10", "2.13.4")

libraryDependencies += "com.raquo" %%% "laminar" % "0.11.0"

npmDependencies in Compile += "@material/mwc-button" -> "0.18.0"

npmDependencies in Compile += "@material/mwc-linear-progress" -> "0.18.0"

npmDependencies in Compile += "@material/mwc-slider" -> "0.18.0"

npmDependencies in Compile += "vega-embed" -> "6.15.1"
npmDependencies in Compile += "vega" -> "5.19.1"
npmDependencies in Compile += "vega-lite" -> "4.17.0"
npmDependencies in Compile += "vega-view" -> "5.9.2"


scalaJSUseMainModuleInitializer := true

scalaJSLinkerConfig in (Compile, fastOptJS) ~= { _.withSourceMap(false) }

useYarn := true
