enablePlugins(ScalaJSPlugin)

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "2.12.10"

crossScalaVersions := Seq("2.12.10", "2.13.1")

libraryDependencies += "com.raquo" %%% "laminar" % "0.7.2"

scalaJSUseMainModuleInitializer := true

emitSourceMaps := false
