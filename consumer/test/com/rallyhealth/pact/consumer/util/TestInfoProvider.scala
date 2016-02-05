package com.rallyhealth.pact.consumer.util

import com.rallyhealth.pact.consumer.BuildInfoProvider

trait TestInfoProvider extends BuildInfoProvider {

  val providerVersion: String = "1.0.0-test"

  val consumerVersion: String = "1.0.0-test"
}