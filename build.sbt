enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin)

// resolvers += Resolver.sonatypeRepo("snapshots")

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "2.12.5"

crossScalaVersions := Seq("2.11.12", "2.12.5")

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "0.4",
  "com.raquo" %%% "domtestutils" % "0.7" % Test,
  "org.scalatest" %%% "scalatest" % "3.0.5" % Test
)

useYarn := true

requiresDOM in Test := true

scalaJSUseMainModuleInitializer := true

emitSourceMaps := false
