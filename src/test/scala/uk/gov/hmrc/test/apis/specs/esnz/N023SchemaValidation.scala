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

import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.test.apis.data.*

class N023SchemaValidation extends BaseSpec with GuiceOneServerPerSuite with ESNZTestDataNotification {

  Feature(
    "N023 : ICB Child Verification API  returns 400 with error response body to DESNZ on request schema validation failure"
  ) {

    val cases: Seq[(String, JsValue, ResponseErrorCode, ResponseErrorMessage)] = Seq(
      (
        "Error : Nino is missing in request body",
        claimRequestBodyMissing("claimantNino"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : Nino value is empty in request body",
        claimRequestBody(claimantNino = ""),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : Nino invalid regex value in request body",
        claimRequestBody(claimantNino = "AA00A008A"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : FirstName is missing in request body",
        claimRequestBodyMissing("claimantFirstName"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : FirstName value is missing in request body",
        claimRequestBody(claimantFirstName = ""),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : FirstName value is more than 35 char in request body",
        claimRequestBody(claimantFirstName = "LauraLauraLauraLauraLauraLauraLaura1"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : SecondName is missing in request body",
        claimRequestBody(claimantSecondName = ""),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : SecondName value is more than 35 char in request body",
        claimRequestBody(claimantSecondName = "TaylorTaylorTaylorTaylorTaylorTaylor"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : DOB is missing in request body",
        claimRequestBodyMissing("claimantDateOfBirth"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : DOB's invalid regex in request body",
        claimRequestBody(claimantDateOfBirth = "1990/06/27"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : DOB's value is missing  in request body",
        claimRequestBody(claimantDateOfBirth = ""),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : bornOnOrAfter is missing  in request body",
        claimRequestBodyMissing("bornOnOrAfter"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : bornOnOrAfter is invalid regex in request body",
        claimRequestBody(bornOnOrAfter = "27-06-2020"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : DOB's value is incorrect in request body",
        claimRequestBody(claimantDateOfBirth = "1990-09-31"),
        "400 Bad Request",
        "The JSON payload is invalid"
      ),
      (
        "Error : bornOnOrAfter value is incorrect in request body",
        claimRequestBody(bornOnOrAfter = "2020-11-31"),
        "400 Bad Request",
        "The JSON payload is invalid"
      )
    )

    cases.foreach { case (scenarioName, payload, statusCode, responseErrorMessage) =>
      Scenario(scenarioName) {

        Given("ICB Child Verification API  receives a valid request from OGD")
        val apiResponse = apiService.claimPostRequest(validHeaders, payload)

        And("ICB Child Verification API  returns the HTTP status code " + statusCode + " response to DESNZ")
        withClue(s"Status=${apiResponse.status}, Body=${apiResponse.body}\n") {
          statusCodeToString(apiResponse.status) shouldBe statusCode
          val errorResMessage: JsValue = Json.parse(apiResponse.body)
          val message                  = (errorResMessage \ "message").as[String]
          message shouldBe responseErrorMessage
        }
      }
    }
  }
}
