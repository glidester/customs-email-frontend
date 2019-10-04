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


import common.pages._
import acceptance.wiremockstub._
import utils.SpecHelper


class EmailVerifcationErrorSpec extends AcceptanceTestSpec
  with SpecHelper
  with StubSave4Later
  with StubAuthClient
  with StubEmailVerification
  with StubCustomsDataStore
  with StubSubscriptionDisplay
  with StubUpdateVerifiedEmail {

  feature("Show Email confirmed to user when the email address is verified") {

    lazy val randomInternalId = generateRandomNumberString()
    lazy val randomEoriNumber = "GB" + generateRandomNumberString()

    scenario("Show email confirmed page without sending email verification link when user email address is verified") {

      Given("the user has successfully logged in")
      authenticate(randomInternalId, randomEoriNumber)
      save4LaterWithNoData(randomInternalId)
      navigateTo(StartPage)
      verifyCurrentPage(StartPage)
      stubSubscriptionDisplayOkResponse(randomEoriNumber)

      clickOn(StartPage.emailLinkText)
      verifySubscriptionDisplayIsCalled(1, randomEoriNumber)

      When("the user provides an email address to change")
      save4LaterWithData(randomInternalId)(emailDetails)
      enterText(WhatIsYourEmailPage.emailTextFieldId)("b@a.com")
      clickContinue()

      Then("the user should be on 'Check your email address' page")
      verifyCurrentPage(CheckYourEmailAddressPage)
      assertIsTextVisible(CheckYourEmailAddressPage.emailAddressId)("b@a.com")

      When("the user confirms to update the email address")
      stubEmailAlreadyVerified()
      stubVerifiedEmailResponse()
      stubBadRequest()
      clickOn(CheckYourEmailAddressPage.yesEmailAddressCss)
      clickContinue()
      Then("the user should be on 'Sorry, there is a problem with the service' page")
     // verifyCurrentPage(EmailConfirmedPage)
      // assertIsTextVisible(EmailConfirmedPage.verifyEmailConfirmedText)("Sorry, there is a problem with the service")
    }
  }
}
