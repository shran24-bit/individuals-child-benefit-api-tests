import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             % "api-test-runner_3"      % "0.10.0",
    "org.slf4j"               % "slf4j-api"              % "2.0.17",
    "io.rest-assured"         % "rest-assured"           % "6.0.0",
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.2",
    "org.scalatest"          %% "scalatest"              % "3.2.19",
    "uk.gov.hmrc"            %% "totp-generator"         % "1.0.0",
    "org.playframework"      %% "play-pekko-http-server" % "3.0.10"
  ).map(_ % Test)

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-30" % "10.7.0",
    "uk.gov.hmrc" %% "domain-play-30"            % "11.0.0"
  )
}
