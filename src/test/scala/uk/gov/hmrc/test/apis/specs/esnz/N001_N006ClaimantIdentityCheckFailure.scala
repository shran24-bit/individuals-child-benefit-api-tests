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
import play.api.libs.json.*
import play.api.libs.ws.DefaultBodyReadables.readableAsByteArray
import uk.gov.hmrc.test.apis.data.*
import scala.language.postfixOps

class N001_N006ClaimantIdentityCheckFailure extends BaseSpec with GuiceOneServerPerSuite with ESNZTestDataNotification {

  Feature(
    "N001_N006 : Claimant identity check failure"
  ) {

    val cases: Seq[(String, JsValue, StatusCode)] = Seq(
      (
        "Failure : N001_Claimant identity check fails due to first name mismatch in record",
        claimRequestBody("Hanna", "White", "1990-06-27", "AA000012A", "2024-06-27"),
        404
      ),
      (
        "Failure : N002_Claimant identity check fails due to second  name mismatch in record",
        claimRequestBody("Tom", "And", "1990-06-27", "AA000014A", "2000-01-01"),
        404
      ),
      (
        "Failure : N003_Claimant identity check fails due to date of birth mismatch in record",
        claimRequestBody("Hannah", "White", "1990-06-28", "AA000012A", "2023-05-01"),
        404
      ),
      (
        "Failure : N004_Claimant identity check fails due to nino mismatch in record",
        claimRequestBody("Tom", "Andrews", "1990-06-28", "AA000012B", "2023-05-01"),
        404
      ),
      (
        "Failure : N006_Claimant identity check fails due to citizen details not found in record",
        claimRequestBody("Michael", "Johnson", "1990-06-28", "AA000027A", "2023-05-01"),
        500
      )
    )

    cases.foreach { case (scenarioName, payload, statusCode) =>
      Scenario(scenarioName) {

        Given("ICB Child Verification API  receives a valid child benefit claim request from OGD")
        val apiResponse = apiService.claimPostRequest(validHeaders, payload)

        Then("ICB Child Verification API  returns the HTTP status code " + statusCode + " to DESNZ")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe statusCode
        }

        And("No error response body should be return")
        apiResponse.body mustBe empty

      }
    }
  }
}
