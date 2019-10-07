/*
 * Copyright 2019 HM Revenue & Customs
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

package acceptance.specs

import acceptance.wiremockstub.{StubAuthClient, StubEmailVerification, StubSave4Later, StubSubscriptionDisplay}
import common.pages.{ChangeYourEmailAddressPage, StartPage, YouCannotChangeYourEmailAddressPage}
import utils.SpecHelper

class AmendmentInProgressSpec
  extends AcceptanceTestSpec
  with StubSave4Later
  with StubAuthClient
  with SpecHelper
  with StubEmailVerification
  with StubSubscriptionDisplay {

  feature("Amendment already in progress") {

    lazy val randomInternalId = generateRandomNumberString()
    lazy val randomEoriNumber = "GB" + generateRandomNumberString()

    scenario("User returning to the service within 24 hours after successfully amending the email") {

      Given("the user has successfully amended the email")
      authenticate(randomInternalId, randomEoriNumber)
      save4LaterWithData(randomInternalId)(emailDetailsWithTimestamp)

      When("the user returns to amend the email again within 24 hours")
      navigateTo(StartPage)
      verifyCurrentPage(StartPage)
      clickOn(StartPage.emailLinkText)

      Then("the user should be redirected to 'You cannot change your email address' page")
      verifyCurrentPage(YouCannotChangeYourEmailAddressPage)
    }

    scenario("User returning to the service after 24 hours of successfully amending the email") {

      Given("the user has successfully amended the email")
      authenticate(randomInternalId, randomEoriNumber)
      save4LaterWithData(randomInternalId)(emailDetailsWithTimestampOver24Hours)
      stubSubscriptionDisplayOkResponse(randomEoriNumber)
      stubVerifiedEmailResponse()

      When("the user returns to amend the email again after 24 hours")
      navigateTo(StartPage)
      verifyCurrentPage(StartPage)
      clickOn(StartPage.emailLinkText)

      Then("the user should be on 'Change your email address for CDS' page")
      verifyCurrentPage(ChangeYourEmailAddressPage)
    }
  }
}