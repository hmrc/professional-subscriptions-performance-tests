import sbt._

object Dependencies {
  
  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "performance-test-runner"   % "5.5.0" % Test,
    "io.gatling.highcharts"   %  "gatling-charts-highcharts" % "3.4.2" % Test,
    "io.gatling"              %  "gatling-test-framework"    % "3.4.2" % Test
  )
}