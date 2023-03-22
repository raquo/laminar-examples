// Get Maven releases faster
resolvers ++= Resolver.sonatypeOssRepos("public")

enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin) // only needed for your JS dependencies, not for Laminar

name := "Laminar Examples"

version := "0.1-SNAPSHOT"

normalizedName := "laminarexamples"

organization := "com.raquo"

scalaVersion := Versions.Scala_3

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % Versions.Laminar,
  "com.raquo" %%% "waypoint" % Versions.Waypoint,
  "com.github.plokhotnyuk.jsoniter-scala" %%% "jsoniter-scala-core" % Versions.JsoniterScala,
  // #TODO[Build] Using "provided" for macros instead of "compiler-internal" because IntelliJ does not understand the latter. Not sure if there's any difference.
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % Versions.JsoniterScala % "provided"
)

(installJsdom / version) := Versions.JsDom

(webpack / version) := Versions.Webpack

(startWebpackDevServer / version) := Versions.WebpackDevServer

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:existentials",
)

Compile / npmDependencies += "@material/mwc-button" -> Versions.MaterialWebComponents

Compile / npmDependencies += "@material/mwc-linear-progress" -> Versions.MaterialWebComponents

Compile / npmDependencies += "@material/mwc-slider" -> Versions.MaterialWebComponents

Compile / fastOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) }

Compile / fullOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(true) }

scalaJSUseMainModuleInitializer := true

(Test / requireJsDomEnv) := true

useYarn := true
