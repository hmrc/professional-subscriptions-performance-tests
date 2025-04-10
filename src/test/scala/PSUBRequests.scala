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

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.time.TaxYear

object PSUBRequests extends CsrfHelper with ServicesConfiguration {

  val serviceUrl: String     = baseUrlFor("professional-subscriptions-frontend")
  val authUrl: String        = baseUrlFor("auth-login-stub")
  val currentTaxYear: String = TaxYear.current.startYear.toString

  val getAuthLoginStub: HttpRequestBuilder =
    http("[GET] - /auth-login-stub/gg-sign-in")
      .get(s"$authUrl/auth-login-stub/gg-sign-in")
      .check(status.is(200))

  def postAuthLoginStub(nino: String): HttpRequestBuilder =
    http("[POST] - /auth-login-stub/gg-sign-in")
      .post(s"$authUrl/auth-login-stub/gg-sign-in")
      .formParam("authorityId", "")
      .formParam("gatewayToken", "")
      .formParam("redirectionUrl", s"$serviceUrl/professional-subscriptions")
      .formParam("credentialStrength", "strong")
      .formParam("confidenceLevel", "200")
      .formParam("affinityGroup", "Individual")
      .formParam("nino", nino)
      .check(status.is(303))

  val getIndex: HttpRequestBuilder =
    http("[GET] - /professional-subscriptions")
      .get(s"$serviceUrl/professional-subscriptions")
      .check(status.is(303))

  val getWhichTaxYear: HttpRequestBuilder =
    get("/which-tax-year")

  val postWhichTaxYear: HttpRequestBuilder =
    post("/which-tax-year", Map("value[0]" -> "currentYear"), "/amounts-already-in-tax-code")

  val getAlreadyInTaxCode: HttpRequestBuilder =
    get("/amounts-already-in-tax-code")

  val postAlreadyInTaxCode: HttpRequestBuilder =
    post("/amounts-already-in-tax-code", Map("value" -> "true"), "/re-enter-amounts")

  val getReEnterAmounts: HttpRequestBuilder =
    get("/re-enter-amounts")

  val postReEnterAmounts: HttpRequestBuilder =
    post("/re-enter-amounts", Map("value" -> "true"), "/summary-subscriptions")

  val getSummarySubscriptions: HttpRequestBuilder =
    get("/summary-subscriptions", false)

  val getWhicSubscription: HttpRequestBuilder =
    get("/which-subscription-are-you-claiming-for/" + currentTaxYear + "/0")

  val postWhicSubscription: HttpRequestBuilder =
    post(
      "/which-subscription-are-you-claiming-for/" + currentTaxYear + "/0",
      Map("subscription" -> "100 Women in Finance Association"),
      "/subscription-amount/" + currentTaxYear + "/0"
    )

  val getSubscriptionAmount: HttpRequestBuilder =
    get("/subscription-amount/" + currentTaxYear + "/0")

  val postSubscriptionAmount: HttpRequestBuilder =
    post(
      "/subscription-amount/" + currentTaxYear + "/0",
      Map("value" -> "100"),
      "/employer-contribution/" + currentTaxYear + "/0"
    )

  val getEmployerContribution: HttpRequestBuilder =
    get("/employer-contribution/" + currentTaxYear + "/0")

  val postEmployerContribution: HttpRequestBuilder =
    post(
      "/employer-contribution/" + currentTaxYear + "/0",
      Map("value" -> "true"),
      "/expenses-employer-paid/" + currentTaxYear + "/0"
    )

  val getExpensesEmployerPaid: HttpRequestBuilder =
    get("/expenses-employer-paid/" + currentTaxYear + "/0")

  val postExpensesEmployerPaid: HttpRequestBuilder =
    post("/expenses-employer-paid/" + currentTaxYear + "/0", Map("value" -> "50"), "/summary-subscriptions")

  val getCYA: HttpRequestBuilder =
    get("/check-your-answers", false)

  val getYourEmployer: HttpRequestBuilder =
    get("/your-employer")

  val postYourEmployer: HttpRequestBuilder =
    post("/your-employer", Map("value" -> "true"), "/how-you-will-get-your-expenses")

  val postYourAddress: HttpRequestBuilder =
    post("/your-address", Map("value" -> "true"), "/check-your-answers")

  val getHowYouWillGetYourExpensesPage: HttpRequestBuilder =
    get("/how-you-will-get-your-expenses", false)

  val getSubmission: HttpRequestBuilder =
    get("submission")

  val getYourAddress: HttpRequestBuilder =
    http("[GET] - /your-address")
      .get(s"$serviceUrl/professional-subscriptions/your-address")
      .disableFollowRedirect
      .check(status.is(303))

  private def get(path: String, expectCsrfToken: Boolean = true): HttpRequestBuilder =
    if (expectCsrfToken) {
      http(s"[GET] - $path")
        .get(s"$serviceUrl/professional-subscriptions/$path")
        .check(saveCsrfToken)
        .disableFollowRedirect
        .check(status.is(200))
    } else {
      http(s"[GET] - $path")
        .get(s"$serviceUrl/professional-subscriptions/$path")
        .disableFollowRedirect
        .check(status.is(200))
    }

  private def post(path: String, data: Map[String, String], redirectToPage: String): HttpRequestBuilder =
    http(s"[POST] - $path")
      .post(s"$serviceUrl/professional-subscriptions/$path")
      .formParamMap(data)
      .formParam("csrfToken", f"$${csrfToken}")
      .disableFollowRedirect
      .check(status.is(303))
      .check(header("location").is(s"/professional-subscriptions$redirectToPage"))

}
