package org.rallyhealth.pact.provider

import org.rallyhealth.pact.models.Pact
import org.scalatest._

class Main {

  /**
   * Instantiates PactSpecs which are Pact specific tests that are plugged into scalatest, so we can get the nice test
   * output of success/failures when running the verifyPact sbt task
   * @param t Pair of the pact config and a series of parsed Pacts themselves
   */
  def runPacts(t: (PactConfiguration, Seq[Pact])) = t match {
    case (config, pacts) =>
      val suite = new Sequential(
        pacts.map { pact =>
          new PactSpec(config, pact)
        }: _*
      )
      stats.fullstacks.run(suite)
  }
}
