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
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.testData.*

class N013_N021DownstreamSystemFailures extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "N013_N021 : Handling downstream service failures such as HTTP 500, 503 and 404 responses"
  ) {

    val cases: Seq[(String, JsValue, StatusCode)] = Seq(
      (
        "Failure : N013_Citizen details api service is unavailable",
        claimRequestBody("Michael", "Johnson", "1990-06-27", "BB000001A", "2023-05-01"),
        503
      ),
      (
        "Failure : N014_Citizen details api is down due to internal server error",
        claimRequestBody("Michael", "Johnson", "1990-06-27", "AA000026A", "2023-05-01"),
        500
      ),
      (
        "Failure : N015_Citizen details api service returns 404 Not Found response",
        claimRequestBody("Michael", "Johnson", "1990-06-27", "AA000025A", "2023-05-01"),
        404
      ),
      (
        "Failure : N016_Individual details api service is unavailable ",
        claimRequestBody("Jess", "Bird", "1990-06-27", "AA000019A", "2023-05-01"),
        503
      ),
      (
        "Failure : N017_Individual details api service is down due to internal server error",
        claimRequestBody("Jess", "Bird", "1990-06-27", "AA000020A", "2023-05-01"),
        500
      ),
      (
        "Failure : N018_Individual Relationship Details api returns 404 Not found response",
        claimRequestBody("Kyle", "Parker", "1990-06-27", "AA000028A", "2023-05-01"),
        502
      ),
      (
        "Failure : N019_Child DOB API service is unavailable",
        claimRequestBody("Jess", "Bird", "1990-06-27", "AA000021A", "2023-05-01"),
        503
      ),
      (
        "Failure : N020_Child DOB API Details service is down due to internal server error",
        claimRequestBody("Jess", "Bird", "1990-06-27", "AA000022A", "2023-05-01"),
        500
      ),
      (
        "Failure : N021_Child DOB API returns 404 Not found response",
        claimRequestBody("Raymond", "Reddington", "1990-06-27", "AA000029A", "2023-05-01"),
        200
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

      }
    }
  }
}
