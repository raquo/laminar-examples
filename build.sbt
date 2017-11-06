enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.3",
  "com.raquo.xstream" %%% "xstream" % "0.2.2-SNAPSHOT",
  "com.raquo" %%% "laminar" % "0.1.1-SNAPSHOT",
  "com.raquo" %%% "dombuilder" % "0.3", // @TODO we should not need to import this
  "org.scalatest" %%% "scalatest" % "3.0.3" % Test,
  "com.raquo" %%% "domtestutils" % "0.3" % Test
)

useYarn := true

requiresDOM in Test := true

scalaJSUseMainModuleInitializer := true

emitSourceMaps := false
