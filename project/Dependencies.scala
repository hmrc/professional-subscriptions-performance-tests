import sbt._

object Dependencies {

  private val gatlingVersion = "3.6.1"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "performance-test-runner"   % "6.1.0" % Test,
    "io.gatling.highcharts"   %  "gatling-charts-highcharts" % gatlingVersion % Test,
    "io.gatling"              %  "gatling-test-framework"    % gatlingVersion % Test,
    "uk.gov.hmrc"             %% "tax-year"                  % "4.0.0"
  )
}