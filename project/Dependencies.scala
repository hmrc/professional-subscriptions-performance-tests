import sbt._

object Dependencies {
  
  val compile: Seq[ModuleID] = Seq(
    "io.gatling.highcharts"   %  "gatling-charts-highcharts" % "2.3.1",
    "io.gatling"              %  "gatling-test-framework"    % "2.3.1"
  )
}