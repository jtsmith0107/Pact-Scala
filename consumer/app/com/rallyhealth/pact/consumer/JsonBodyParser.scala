package com.rallyhealth.pact.consumer

import org.rallyhealth.pact.models.Pact

trait JsonBodyParser {

  /**
   * Generic json parser should serialize scala Pact model into a properly formatted string.
   */
  def jsonToStringParser(pact: Pact): String
}

