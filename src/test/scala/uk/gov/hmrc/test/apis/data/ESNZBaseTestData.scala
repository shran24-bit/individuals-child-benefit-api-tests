/*
 * Copyright 2026 HM Revenue & Customs
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


import java.time.LocalDate
import scala.util.Random

trait ESNZBaseTestData {

  def generateParentDob(): String = {
    val today     = LocalDate.now()
    val random    = new Random()
    val age       = 20 + random.nextInt(31)
    val dayOffset = random.nextInt(365)
    val dob       = today.minusYears(age).minusDays(dayOffset)
    dob.toString
  }

  def generateChildDob(): String = {
    val startDate  = LocalDate.parse("2025-12-01")
    val random     = new Random()
    val childAge   = random.nextInt(6) // 0 to 5 years
    val randomDays = random.nextInt(365)
    val childDob   = startDate.minusYears(childAge).minusDays(randomDays)
    childDob.toString

  }

}

def constraintViolation(field: String) =
  s"Constraint Violation - Invalid/Missing input parameter: $field"

// Headers

val validHeaders: Seq[(String, String)] =
  Seq(
    "Accept"       -> "application/vnd.hmrc.1.0+json",
    "Content-Type" -> "application/json"
  )

def removeHeader(headers: Seq[(String, String)], key: String): Seq[(String, String)] =
  headers.filterNot(_._1.equalsIgnoreCase(key))

def overrideHeader(headers: Seq[(String, String)], key: String, value: String): Seq[(String, String)] =
  removeHeader(headers, key) :+ (key -> value)

// Missing headers
def headersMissingContentType: Seq[(String, String)] =
  removeHeader(validHeaders, "Content-Type")

def headersMissingAuthorization: Seq[(String, String)] =
  removeHeader(validHeaders, "Authorization")

def headersMissingAccept: Seq[(String, String)] =
  removeHeader(validHeaders, "Accept")

def headersMissingCorrelationId: Seq[(String, String)] =
  removeHeader(validHeaders, "correlationId")

// Invalid headers
def headersInvalidContentType: Seq[(String, String)] =
  overrideHeader(validHeaders, "Content-Type", "INVALID")

def headersInvalidAuth: Seq[(String, String)] =
  overrideHeader(validHeaders, "Authorization", "Bearer 1234")

def headersInvalidAccept: Seq[(String, String)] =
  overrideHeader(validHeaders, "Accept", "application/json")

def headersInvalidCorrelationId: Seq[(String, String)] =
  overrideHeader(validHeaders, "correlationId", "2db2a28e-52c9-4814-8a94")

// Empty headers
def headersEmptyContentType: Seq[(String, String)] =
  overrideHeader(validHeaders, "Content-Type", "")

def headersEmptyAuth: Seq[(String, String)] =
  overrideHeader(validHeaders, "Authorization", "")

def headersEmptyAccept: Seq[(String, String)] =
  overrideHeader(validHeaders, "Accept", "")

def headersEmptyCorrelationId: Seq[(String, String)] =
  overrideHeader(validHeaders, "correlationId", "")

val getExpiredAuthToken: String =
  "GNAP dummy-2b5998dccb61446fa2fa9d0f7211e181,Bearer JDkThJhFPxDJlUABxXqtpxYmzIwWik1RYVp61xoEcLudlSq6higSU7OEQEqBlgTBQHTNWWRzZl4T8m8tfArtX2o7gA/qYAHhfHqWOxAp0flCPkSIhfyXuZyHTfLRXSQ8i6AejDV6nH4Td0KWtpjTSzP05ue6FHXdOIOSD7ZvHOwYgyplVeOQ7qHLUwzFwxEW/SsCJHiDyv5jJQREo7nuFQQ"

// Types
type ResponseErrorCode      = String
type ResponseErrorMessage   = String
type ResponseSuccessMessage = String
type StatusCode             = Int
def statusCodeToString(statusCode:Int ):String = {
  statusCode match {
    case 400 => "400 Bad Request"
  }
}

val InvalidInputCode: ResponseErrorCode = "400.1"
val ForbiddenCode: ResponseErrorCode    = "403.2"

val liabilityStartDate: String = "2025-08-19"
val liabilityEndDate: String   = "2026-06-30"
