// Get Maven releases faster
resolvers ++= Resolver.sonatypeOssRepos("public")

enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin) // only needed for your JS dependencies, not for Laminar

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "2.13.10"

crossScalaVersions := Seq("2.12.17", "2.13.10", "3.2.0")

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "15.0.0-M1",
  "com.raquo" %%% "waypoint" % "6.0.0-M1",
  "com.lihaoyi" %%% "upickle" % "2.0.0"
)

(installJsdom / version) := "20.0.3"

(webpack / version) := "5.75.0"

(startWebpackDevServer / version) := "4.11.1"

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

Compile / fastOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) }

Compile / fullOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) }

scalaJSUseMainModuleInitializer := true

(Test / requireJsDomEnv) := true

useYarn := true
