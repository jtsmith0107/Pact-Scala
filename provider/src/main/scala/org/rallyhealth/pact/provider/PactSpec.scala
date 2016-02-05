package org.rallyhealth.pact.provider

import org.rallyhealth.pact.models.Pact


/**
 * Intended to consume the Pact for testing against the environement provided by the configuration. This effectively
 * @param configuration Service/Provider Environment to verify against
 * @param pact The parsed Pact provided by json files
 */
class PactSpec(configuration: PactConfiguration, pact: Pact)  {


//  pact.interactions.toList.map { interaction =>
//    s"""pact for consumer ${pact.consumer.name}
//        |provider ${pact.provider.name}
//        |interaction "${interaction.description}"
//                                                  |in state: "${interaction.providerState.getOrElse("")}" """
//      .stripMargin in {

      //      val stateChangeFuture = (config.stateChangeUrl, interaction.providerState) match {
      //        case (Some(stateChangeUrl), Some(providerState)) => HttpClient
      //          .run(EnterStateRequest(stateChangeUrl.url, providerState))
      //        case (_, _) => Future()
      //      }
      //
      //      val pactResponseFuture: Future[PactResponse] = for {
      //        _ <- stateChangeFuture
      //        response <- HttpClient.run(ServiceInvokeRequest(config.providerRoot.url, interaction.request))
      //      } yield response
      //
      //      val actualResponse = Await.result(pactResponseFuture, timeout)
      //
      //      val responseMismatches = ResponseMatching.responseMismatches(interaction.response, actualResponse)
      //      if (!responseMismatches.isEmpty) {
//      throw new TestFailedException(s"There were response mismatches: \n${responseMismatches.mkString("\n")}", 10)
//    }

}
