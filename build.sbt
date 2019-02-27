enablePlugins(ScalaJSPlugin)

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "2.12.8"

libraryDependencies +=   "com.raquo" %%% "laminar" % "0.6"

scalaJSUseMainModuleInitializer := true

emitSourceMaps := false
