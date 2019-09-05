import PSUBRequests._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}

class PSUBSimulation extends Simulation {

  import Config._

  private def waitABit: ChainBuilder = pause(minPause, maxPause)

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

        getCYA, waitABit,
        postCYA("/confirmation-current-year")

          getYourEmployer, waitABit,
          postYourEmployer, waitABit,

          getHowYouWillGetYourExpensesPage, waitABit,

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
