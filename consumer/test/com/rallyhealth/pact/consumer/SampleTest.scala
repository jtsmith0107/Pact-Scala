package com.rallyhealth.pact.consumer

import com.rallyhealth.pact.consumer.util.TestInfoProvider
import org.rallyhealth.pact.models.{Interaction, Pact, PactRequest, PactResponse}

class SampleTest extends PactSpec with TestInfoProvider {

  val pactName: String = "SampleTest"


  it should "do a thing" in withInteraction(
    Interaction(
      None, "", PactRequest[String]("", "", None, None, "None"), PactResponse[String](1, None, "None")
    )
  ) { i =>
    val x = i.request.body


    assert(1 + 1 === 2)
  }

  it should "also do a thing" in withInteraction(
    Interaction(
      None, "", PactRequest("", "iiuiurerewuiorewre", None, None, None), PactResponse(2, None, None)
    )
  )
  { i =>
    assert(1 + 1 === 2)
  }

  /**
   * Generic json parser should serialize scala Pact model into a properly formatted string.
   */
  override def jsonToStringParser(pact: Pact): String = pact.toString
}