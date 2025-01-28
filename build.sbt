val appName = "professional-subscriptions-performance-tests"
val appVersion = "0.1.0-SNAPSHOT"

lazy val root = Project(appName, file("."))
  .enablePlugins(GatlingPlugin)
  .enablePlugins(CorePlugin)
  .enablePlugins(JvmPlugin)
  .enablePlugins(IvyPlugin)
  .settings(
    organization := "uk.gov.hmrc",
    name := appName,
    version := appVersion,
    libraryDependencies ++= Dependencies.compile,
    scalaVersion := "2.13.12",
    scalacOptions ++= Seq("-feature", "-language:implicitConversions", "-language:postfixOps"),
    retrieveManaged := true,
    initialCommands in console := "import uk.gov.hmrc._",
    parallelExecution in Test := false,
    publishArtifact in Test := true,
    testOptions in Test := Seq.empty
  )
