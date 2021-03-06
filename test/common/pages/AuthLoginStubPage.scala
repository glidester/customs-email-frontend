/*
 * Copyright 2021 HM Revenue & Customs
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

package common.pages

import utils.TestEnvironment._
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.Select
import utils.{Configuration, SpecHelper}

class AuthLoginStubPage extends BasePage with SpecHelper {

  override val url: String = Configuration.forCurrentEnv {
    case QA    => "https://www.qa.tax.service.gov.uk"
    case DEV   => "https://www.development.tax.service.gov.uk"
    case LOCAL => "http://localhost:9949"
  } + "/auth-login-stub/gg-sign-in"

  override val title = "Authority Wizard"

  val credIdName: By = By.name("authorityId")
  val redirectUrlName: By = By.name("redirectionUrl")
  val enrolmentKey: By = By.name("enrolment[0].name")
  val identifierName: By = By.id("input-0-0-name")
  val identifierValue: By = By.id("input-0-0-value")

  val submitButtonCss: By = By.cssSelector(".button")

  def login(
      credId: String,
      affinityType: String = "Individual",
      credentialRole: String = "User",
      eori: String = "ZZ123456789000"
  ): Unit = {
    enterText(credIdName)(credId)

    val affinityDropDown = webDriver.findElement(By.name("affinityGroup"))
    val selectAffinity = new Select(affinityDropDown)
    selectAffinity.selectByValue(affinityType)

    val credentialRoleDropDown =
      webDriver.findElement(By.name("credentialRole"))
    val selectCredentialRole = new Select(credentialRoleDropDown)
    selectCredentialRole.selectByValue(credentialRole)

    enterText(redirectUrlName)(
      Configuration.frontendHost + "/manage-email-cds/start")
    enterText(enrolmentKey)("HMRC-CUS-ORG")
    enterText(identifierName)("EORINumber")
    enterText(identifierValue)(eori)

    clickOn(submitButtonCss)
  }

}

object AuthLoginStubPage extends AuthLoginStubPage
