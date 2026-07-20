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

class N022HeaderAcceptValidation extends BaseSpec with GuiceOneServerPerSuite with ESNZTestDataNotification {

  Feature(
    "N022 : Header Validation Scenario : Accept validation Failure"
  ) {

    val cases: Seq[(String, Seq[(String, String)], StatusCode)] = Seq(
      (
        "Error : Accept is invalid in request header",
        headersInvalidAccept,
        406
      ),
      (
        "Error : Accept is missing in request header",
        headersMissingAccept,
        406
      ),
      (
        "Error : Accept empty in request header",
        headersEmptyAccept,
        406
      )
    )

    cases.foreach { case (scenarioName, headers, statusCode) =>
      Scenario(scenarioName) {

        Given("ICB Child Verification API  receives a valid request from OGD")
        val apiResponse = apiService.claimPostRequestWithoutAuth(headers, validRequestBody())

        Then("ICB Child Verification API  returns the HTTP status code " + statusCode + " response to DESNZ")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          apiResponse.status mustBe statusCode
        }

         And("Error response body must contain correct error details")
        val responseBody = Json.parse(apiResponse.body)
        (responseBody \ "errors[0].code").as[String] mustBe "ACCEPT_HEADER_INVALID"
        (responseBody \ "errors[0].message")
          .as[String] mustBe "The accept header is missing or invalid"

      }
    }
  }
}
