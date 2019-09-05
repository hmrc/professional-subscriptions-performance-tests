import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object PSUBRequests extends CsrfHelper {

  val serviceUrl: String = Config.baseUrl + "/professional-subscriptions"

  val getAuthLoginStub: ChainBuilder =
    exec(
      http("[GET] - /auth-login-stub/gg-sign-in")
        .get(Config.authUrl)
        .check(status.is(200))
    )

  def postAuthLoginStub(nino: String): ChainBuilder =
    exec(
      http("[POST] - /auth-login-stub/gg-sign-in")
        .post(Config.authUrl)
        .formParam("authorityId", "")
        .formParam("gatewayToken", "")
        .formParam("redirectionUrl", serviceUrl)
        .formParam("credentialStrength", "weak")
        .formParam("confidenceLevel", "200")
        .formParam("affinityGroup", "Individual")
        .formParam("nino", nino)
        .check(status.is(200))
    )


  val getWhichTaxYear: ChainBuilder =
    get("/which-tax-year")
  val postWhichTaxYear: ChainBuilder =
    post("/which-tax-year", Map("value[0]" -> "currentYear"), "/amounts-already-in-tax-code")
  val getAlreadyInTaxCode: ChainBuilder =
    get("/amounts-already-in-tax-code")
  val postAlreadyInTaxCode: ChainBuilder =
    post("/amounts-already-in-tax-code", Map("value" -> "true"), "/re-enter-amounts")
  val getReEnterAmounts: ChainBuilder =
    get("/re-enter-amounts")
  val postReEnterAmounts: ChainBuilder =
    post("/re-enter-amounts", Map("value" -> "true"), "/summary-subscriptions")
  val getSummarySubscriptions: ChainBuilder =
    get("/summary-subscriptions", false)
  val getWhicSubscription: ChainBuilder =
    get("/which-subscription-are-you-claiming-for/2019/0")
  val postWhicSubscription: ChainBuilder =
    post("/which-subscription-are-you-claiming-for/2019/0", Map("subscription" -> "100 Women in Finance Association"), "/subscription-amount/2019/0")
  val getSubscriptionAmount: ChainBuilder =
    get("/subscription-amount/2019/0")
  val postSubscriptionAmount: ChainBuilder =
    post("/subscription-amount/2019/0", Map("value" -> "100"), "/employer-contribution/2019/0")
  val getEmployerContribution: ChainBuilder =
    get("/employer-contribution/2019/0")
  val postEmployerContribution: ChainBuilder =
    post("/employer-contribution/2019/0", Map("value" -> "true"), "/expenses-employer-paid/2019/0")
  val getExpensesEmployerPaid: ChainBuilder =
    get("/expenses-employer-paid/2019/0")
  val postExpensesEmployerPaid: ChainBuilder =
    post("/expenses-employer-paid/2019/0", Map("value" -> "50"), "/summary-subscriptions")
  val getCYA: ChainBuilder =
    get("/check-your-answers", false)
  val getYourEmployer: ChainBuilder =
    get("/your-employer")
  val postYourEmployer: ChainBuilder =
    post("/your-employer", Map("value" -> "true"), "/your-address")
  val getYourAddress: ChainBuilder =
    get("/your-address")
  val postYourAddress: ChainBuilder =
    post("/your-address", Map("value" -> "true"), "/check-your-answers")
  val getHowYouWillGetYourExpensesPage: ChainBuilder =
    get("/how-you-will-get-your-expenses")
  val getSubmission: ChainBuilder =
    get("submission")


  private def get(path: String, expectCsrfToken: Boolean = true): ChainBuilder =
    if (expectCsrfToken) {
      exec(
        http(s"[GET] - $path")
          .get(s"$serviceUrl$path")
          .check(saveCsrfToken)
          .disableFollowRedirect
          .check(status.is(200))
      )
    } else {
      exec(
        http(s"[GET] - $path")
          .get(s"$serviceUrl$path")
          .disableFollowRedirect
          .check(status.is(200))
      )
    }

  private def post(path: String, data: Map[String, String], redirectToPage: String): ChainBuilder =
    exec(
      http(s"[POST] - $path")
        .post(s"$serviceUrl$path")
        .formParamMap(data)
        .formParam("csrfToken", f"$${csrfToken}")
        .disableFollowRedirect
        .check(status.is(303))
        .check(header("location").is(s"/professional-subscriptions$redirectToPage"))
    )
}
