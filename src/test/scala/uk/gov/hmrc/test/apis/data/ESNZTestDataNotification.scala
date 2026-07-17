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

package uk.gov.hmrc.test.apis.data

import play.api.libs.json.{JsObject, Json}

trait ESNZTestDataNotification extends ESNZBaseTestData {

  def claimRequestBody(
    claimantFirstName: String = "firstName",
    claimantSecondName: String = "secondName",
    claimantDateOfBirth: String = "dateOfBirth",
    claimantNino: String = "nino",
    bornOnOrAfter: String = "bornOnOrAfter"
  ): JsObject = Json.obj(
    "firstName"     -> claimantFirstName,
    "secondName"    -> claimantSecondName,
    "dateOfBirth"   -> claimantDateOfBirth,
    "nino"          -> claimantNino,
    "bornOnOrAfter" -> bornOnOrAfter
  )

  def validRequestBody(
    claimantFirstName: String = "Laura",
    claimantSecondName: String = "Taylor",
    claimantDateOfBirth: String = "1990-06-27",
    claimantNino: String = "AA000008A",
    bornOnOrAfter: String = "2025-12-01"
  ): JsObject = Json.obj(
    "firstName"     -> claimantFirstName,
    "secondName"    -> claimantSecondName,
    "dateOfBirth"   -> claimantDateOfBirth,
    "nino"          -> claimantNino,
    "bornOnOrAfter" -> bornOnOrAfter
  )

  def claimRequestBodyMissing(parameterName: String): JsObject =
    claimRequestBody() - parameterName

}
