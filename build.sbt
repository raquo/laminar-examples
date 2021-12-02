// Get Maven releases faster
resolvers ++= Resolver.sonatypeOssRepos("public")

enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin) // only needed for your JS dependencies, not for Laminar

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := "3.2.0"

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "15.0.0-M6",
  "com.raquo" %%% "waypoint" % "6.0.0-M4",
  "com.github.plokhotnyuk.jsoniter-scala" %%% "jsoniter-scala-core" % "2.20.3",
  // #TODO[Build] Using "provided" for macros instead of "compiler-internal" because IntelliJ does not understand the latter. Not sure if there's any difference.
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.20.3" % "provided"
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

Compile / fullOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(true) }

scalaJSUseMainModuleInitializer := true

(Test / requireJsDomEnv) := true

useYarn := true
