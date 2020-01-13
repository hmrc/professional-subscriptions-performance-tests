resolvers += Resolver.url("hmrc-sbt-plugin-releases",
  url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "2.5.0")

addSbtPlugin("io.gatling" % "gatling-sbt" % "2.1.7")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.25")

addSbtPlugin("uk.gov.hmrc" % "sbt-settings" % "4.1.0")