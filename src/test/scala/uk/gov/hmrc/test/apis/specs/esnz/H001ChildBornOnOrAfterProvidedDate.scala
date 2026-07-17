/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.test.apis.specs.esnz

import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json
import play.api.libs.json.*
import uk.gov.hmrc.test.apis.data.*

class H001ChildBornOnOrAfterProvidedDate extends BaseSpec with GuiceOneServerPerSuite with ESNZTestDataNotification {

  Feature(
    "H001 : Claimant's child is born on or after the provided date"
  ) {

    val cases: Seq[(String, JsValue, StatusCode)] = Seq(
      (
        "Success : 1 child DOB on the provided date",
        claimRequestBody("Laura", "Taylor", "1990-06-27", "AA000008A", "2025-12-01"),
        200
      ),
      (
        "Success : 2 Children DOB After & Before Provided Date",
        claimRequestBody("Robert", "Anderson", "1990-06-27", "AA000007A", "2025-12-31"),
        200
      ),
      (
        "Success : 2 Children DOB After ProvidedDate with DOD",
        claimRequestBody("Daniel", "Jackson", "1990-06-27", "AA000011A", "2018-01-01"),
        200
      ),
      (
        "Success : 4 Children DOB After & Before Provided Date",
        claimRequestBody("Tom", "Andrews", "1990-06-27", "AA000014A", "2023-05-01"),
        200
      ),
      (
        "Success : 3 Children DOB On Provided Date with DOD",
        claimRequestBody("Hannah", "White", "1990-06-27", "AA000012A", "2024-06-27"),
        200
      )
    )

    cases.foreach { case (scenarioName, payload, statusCode) =>
      Scenario(scenarioName) {

        Given("ICB Child Verification API  receives a valid request from OGD")
        val apiResponse = apiService.claimPostRequest(validHeaders, payload)

        Then("ICB Child Verification API  returns the HTTP status code " + statusCode + " response to DESNZ")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe statusCode
        }

        And("Success response body must contain correct error details")
        val responseBody = Json.parse(apiResponse.body)
        (responseBody \ "eligible").as[Boolean] shouldBe true
      }
    }
  }
}
