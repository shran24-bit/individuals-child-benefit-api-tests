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
import uk.gov.hmrc.test.apis.data.*

class N007_N009IndividualRelationshipDetailsFailure
    extends BaseSpec
    with GuiceOneServerPerSuite
    with ESNZTestDataNotification {

  Feature(
    "N007_N009 : Claimant's relationship type and source check failure"
  ) {

    val cases: Seq[(String, JsValue, StatusCode)] = Seq(
      (
        "Failure : N007_Claimant's relationship type and source are not classified as expected",
        claimRequestBody("Michael", "Johnson", "1990-06-27", "AA000001A", "2023-05-01"),
        200
      ),
      (
        "Failure : N008_Claimant's relationship source is not classified as expected",
        claimRequestBody("Jess", "Bird", "1990-06-27", "AA000018A", "2023-05-01"),
        200
      ),
      (
        "Failure : N008_Claimant's relationship data not found",
        claimRequestBody("Frank", "Smith", "1990-06-27", "AA000016A", "2023-05-01"),
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

        And("Error response body must contain correct error details")
        val responseBody = Json.parse(apiResponse.body)
        (responseBody \ "eligible").as[Boolean] shouldBe false

      }
    }
  }
}
