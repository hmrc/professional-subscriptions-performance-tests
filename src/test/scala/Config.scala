/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import scala.concurrent.duration._
import scala.language.postfixOps

object Config {

  val baseUrl: String =
    sys.props.get("perftest.baseUrl").getOrElse(
      if (runLocal) "http://localhost:9335" else ("https://www.staging.tax.service.gov.uk")
    )

  val authUrl: String =
    sys.props.get("perftest.authUrl").getOrElse(
      if (runLocal) "http://localhost:9949/auth-login-stub/gg-sign-in" else "https://www.staging.tax.service.gov.uk/auth-login-stub/gg-sign-in"
    )

  val minPause: Duration =
    sys.props.get("perftest.minPause").map(toDuration).getOrElse(1 second)

  val maxPause: Duration =
    sys.props.get("perftest.maxPause").map(toDuration).getOrElse(1 second)

  val totalUsers: Int =
    sys.props.get("perftest.totalUsers").map(_.toInt).getOrElse(10)

  val rampUpTime: FiniteDuration =
    sys.props.get("perftest.rampupTime").map(toDuration).getOrElse(1 minute)

  val constantRateTime: FiniteDuration =
    sys.props.get("perftest.contantRateTime").map(toDuration).getOrElse(1 minute)

  val rampDownTime: FiniteDuration =
    sys.props.get("perftest.rampdownTime").map(toDuration).getOrElse(1 minute)

  val loadPercentage: Double =
    sys.props.get("perftest.loadPercentage").map(_.toDouble / 100).getOrElse(1D)

  val totalTime: FiniteDuration =
    rampUpTime + constantRateTime + rampDownTime

  val runSmokeTest: Boolean =
    sys.props.get("perftest.runSmokeTest").map(_.toBoolean).getOrElse(false)

  val runLocal: Boolean =
    sys.props.get("perftest.runLocal").map(_.toBoolean).getOrElse(false)

  val maxTps: Int = 50

  private def toDuration(string: String): FiniteDuration =
    Duration(string).asInstanceOf[FiniteDuration]
}
