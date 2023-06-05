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

import PSUBRequests._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

class PSUBSimulation extends Simulation {

  import Config._

  private def waitABit: ChainBuilder = pause(minPause.toString, maxPause.toString)

  private val psubAddAProfessionalSubscription: ScenarioBuilder =
    scenario("Happy Path")
      .exec(
        getAuthLoginStub, waitABit,
        postAuthLoginStub("AB216913B"), waitABit,

        getWhichTaxYear, waitABit,
        postWhichTaxYear, waitABit,

        getAlreadyInTaxCode, waitABit,
        postAlreadyInTaxCode, waitABit,

        getReEnterAmounts, waitABit,
        postReEnterAmounts, waitABit,

        getSummarySubscriptions, waitABit,

        getWhicSubscription, waitABit,
        postWhicSubscription, waitABit,

        getSubscriptionAmount, waitABit,
        postSubscriptionAmount, waitABit,

        getEmployerContribution, waitABit,
        postEmployerContribution, waitABit,

        getExpensesEmployerPaid, waitABit,
        postExpensesEmployerPaid, waitABit,

        getSummarySubscriptions, waitABit,

        getYourAddress, waitABit,

        getCYA, waitABit,

        getYourEmployer, waitABit,
        postYourEmployer, waitABit,

        getHowYouWillGetYourExpensesPage, waitABit
      )

  private val tps = (maxTps * loadPercentage).toInt

  if (runSmokeTest) {
    setUp(
      psubAddAProfessionalSubscription.inject(atOnceUsers(1))

    ).protocols(http)
  } else {
    setUp(
      psubAddAProfessionalSubscription.inject(rampUsersPerSec(1) to totalUsers during rampUpTime)
    ).protocols(http).throttle(
      reachRps(tps) in rampUpTime,
      holdFor(constantRateTime),
      reachRps(0) in rampDownTime
    )
  }
}
