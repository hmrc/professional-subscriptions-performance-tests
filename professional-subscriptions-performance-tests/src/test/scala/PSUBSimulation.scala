import EERequests._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

class PSUBSimulation extends Simulation {

  import Config._

  private def waitABit: ChainBuilder = pause(minPause, maxPause)

  private val psubAddAProfessionalSubscription: ScenarioBuilder =
    scenario("Previous Year")
      .exec(
        getAuthLoginStub, waitABit,
        postAuthLoginStub("LL111111B"), waitABit,

        getWhichTaxYear, waitABit,
        postgetWhichTaxYear, waitABit,

        getAlreadyInTaxCode, waitABit,
        postAlreadyInTaxCode, waitABit,

        getAmountsYouNeedToChange, waitABit,
        postAmountsYouNeedToChange, waitABit,

        getSummarySubscriptions, waitABit,
        postgetSummarySubscriptions, waitABit,

        getWhicSubscription, waitABit,
        postWhicSubscription, waitABit,

        getAlreadyClaimingDifferentAmounts, waitABit,
        postAlreadyClaimingDifferentAmounts, waitABit,

        getEmployerContribution, waitABit,
        postEmployerContribution, waitABit,

        getExpensesEmployerPaid, waitABit,
        postExpensesEmployerPaid, waitABit,

        getSummarySubscriptions, waitABit,

        getYourEmployer, waitABit,
        postYourEmployer, waitABit,


        getYourAddress, waitABit,
        postYourAddress, waitABit,
        getCYA, waitABit,

        postCYA("/confirmation")
      )


  private val tps = (maxTps * loadPercentage).toInt

  if (runSmokeTest) {
    setUp(
      eePreviousYearRequests.inject(atOnceUsers(1)),
      eeCurrentYearRequests.inject(atOnceUsers(1)),
      eeCurrentPreviousYearRequests.inject(atOnceUsers(1)),
      eeStopped.inject(atOnceUsers(1))
    ).protocols(http)
  } else {
    setUp(
      eePreviousYearRequests.inject(rampUsersPerSec(1) to totalUsers during rampUpTime),
      eeCurrentYearRequests.inject(rampUsersPerSec(1) to totalUsers during rampUpTime),
      eeCurrentPreviousYearRequests.inject(rampUsersPerSec(1) to totalUsers during rampUpTime),
      eeStopped.inject(rampUsersPerSec(1) to totalUsers during rampUpTime)
    ).protocols(http).throttle(
      reachRps(tps) in rampUpTime,
      holdFor(constantRateTime),
      reachRps(0) in rampDownTime
    )
  }
}
