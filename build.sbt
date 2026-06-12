val appName    = "professional-subscriptions-performance-tests"
val appVersion = "0.1.0-SNAPSHOT"

lazy val root = Project(appName, file("."))
  .enablePlugins(GatlingPlugin)
  .enablePlugins(CorePlugin)
  .enablePlugins(JvmPlugin)
  .enablePlugins(IvyPlugin)
  .settings(
    organization := "uk.gov.hmrc",
    name         := appName,
    version      := appVersion,
    libraryDependencies ++= Dependencies.compile,
    scalaVersion := "3.3.7",
    scalacOptions ++= Seq("-feature", "-language:implicitConversions"),
    retrieveManaged            := true,
    console / initialCommands  := "import uk.gov.hmrc.*",
    Test / parallelExecution  := false,
    Test / publishArtifact    := true,
    Test / testOptions        := Seq.empty
  )
