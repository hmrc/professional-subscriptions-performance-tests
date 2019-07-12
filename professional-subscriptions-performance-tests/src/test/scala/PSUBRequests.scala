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
    post("/which-tax-year", Map("value" -> "currentYear"), "/amounts-already-in-tax-code")

  val getAlreadyInTaxCode: ChainBuilder =
    get("/amounts-already-in-tax-code")
  val postAlreadyInTaxCase: ChainBuilder =
    post("/amounts-already-in-tax-code", Map("value" -> "false"), "/amounts-you-need-to-change")

  val getAmountsYouNeedToChange: ChainBuilder =
    get("/amounts-you-need-to-change")
  val postAmountsYouNeedToChange: ChainBuilder =
    post("/amounts-you-need-to-change", Map("value" -> "currentYear"), "/summary-subscriptions")

  val getSummarySubscriptions: ChainBuilder =
    get("/summary-subscriptions", false)
  val postSummarySubscriptions: ChainBuilder =
    post("/summary-subscriptions", "/which-subscription-are-you-claiming-for/2019/0")

  val getWhicSubscription: ChainBuilder =
    get("/which-subscription-are-you-claiming-for/2019/0")
  def postWhicSubscription(formData: Map[String, String], redirect: String), subscription: String): ChainBuilder =
    .formParam("subscription", "")
    post("/which-subscription-are-you-claiming-for/2019/0", formData, redirect)

  val getAlreadyClaimingDifferentAmounts: ChainBuilder =
    get("/subscription-amount/2019/0")
  def postAlreadyClaimingDifferentAmounts(formData: Map[String, String], redirect: String), amount: String): ChainBuilder =
  .formParam("value", "")
  post("/subscription-amount/2019/0", formData, redirect)

  val getEmployerContribution: ChainBuilder =
    get("/employer-contribution/2019/0")
  val postEmployerContribution: ChainBuilder =
    post("/employer-contribution/2019/0", Map("value" -> "true"), "/expenses-employer-paid/2019/0")

  val getExpensesEmployerPaid: ChainBuilder =
    get("//expenses-employer-paid/2019/0")
  def postExpensesEmployerPaid(formData: Map[String, String], redirect: String), subscription: String): ChainBuilder =
  .formParam("value", "")
  post("//expenses-employer-paid/2019/0", formData, redirect)

  val getSummarySubscriptions: ChainBuilder =
    get("/summary-subscriptions", false)


  val getYourEmployer: ChainBuilder =
    get("/your-employer")
  val postYourEmployer: ChainBuilder =
    post("/your-employer", Map("value" -> "true"), "/your-address")

  val getYourAddress: ChainBuilder =
    get("/your-address")
  val postYourAddress: ChainBuilder =
    post("/your-address", Map("value" -> "true"), "/check-your-answers")

  val getCYA: ChainBuilder =
    get("/check-your-answers", false)
  def postCYA(redirect: String): ChainBuilder =
    post("/check-your-answers", Map("" -> ""), redirect)

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
