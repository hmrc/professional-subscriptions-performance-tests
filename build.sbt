
lazy val root = (project in file("."))
  .enablePlugins(GatlingPlugin)
  .settings(
    libraryDependencies ++= Dependencies.compile,
    scalaVersion := "2.12.4",
    fork in Test := true
  )