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

import utils.Configuration

abstract class IneligibleUserPage extends BasePage {
  override val title = "You cannot use this service"
}

object IneligibleUserPage extends IneligibleUserPage {
  override val url
    : String = Configuration.frontendHost + "/manage-email-cds/ineligible/no-enrolment"
}

object IneligibleUserNotAdminPage extends IneligibleUserPage {
  override val url
    : String = Configuration.frontendHost + "/manage-email-cds/ineligible/not-admin"
}

object IneligibleUserAgentPage extends IneligibleUserPage {
  override val url
    : String = Configuration.frontendHost + "/manage-email-cds/ineligible/is-agent"
}
