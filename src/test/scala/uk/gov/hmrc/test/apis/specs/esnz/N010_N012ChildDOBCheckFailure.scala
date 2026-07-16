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
import play.api.libs.ws.DefaultBodyReadables.readableAsByteArray
import uk.gov.hmrc.api.testData.*

class N010_N012ChildDOBCheckFailure extends BaseSpec with GuiceOneServerPerSuite with TestDataNotification {

  Feature(
    "N010_N012 : Claimant's child DOB check failure"
  ) {

    val cases: Seq[(String, JsValue, StatusCode)] = Seq(
      (
        "Failure : N010_Claimant's child is born before the provided date",
        claimRequestBody("James", "Brown", "1990-06-27", "AA000003A", "2024-06-28"),
        200
      ),
      (
        "Failure : N012_Claimant's children data not found ",
        claimRequestBody("Jess", "Bird", "1990-06-27", "AA000017A", "2025-06-01"),
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

        /*And("Error response body must contain correct error details")
        if (Scenario.toString() == "N010") {
          val responseBody = Json.parse(apiResponse.body)
          (responseBody \ "eligible").as[Boolean] shouldBe false
        } else { apiResponse.body mustBe empty }*/

      }
    }
  }
}
