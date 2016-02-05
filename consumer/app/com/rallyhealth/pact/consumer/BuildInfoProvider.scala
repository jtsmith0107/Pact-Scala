package com.rallyhealth.pact.consumer

trait BuildInfoProvider {

  def providerVersion: String

  def consumerVersion: String
}