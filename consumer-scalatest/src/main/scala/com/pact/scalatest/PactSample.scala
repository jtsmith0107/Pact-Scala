package com.pact.scalatest

import au.com.dius.pact.consumer.dsl.{PactDslJsonBody, PactDslWithProvider}
import au.com.dius.pact.consumer.{ConsumerPactBuilder, PactVerified, VerificationResult, _}
import au.com.dius.pact.model.{MockProviderConfig, PactConfig, PactFragment, PactSpecVersion}
import com.pact.models.MockedExchange
import scala.reflect.runtime.{universe => ru}
import ru._
import org.scalatest.{FlatSpec, Matchers}


trait HasPactDslProvider {
  def dslProvider: PactDslWithProvider
}

trait HasProviderConfig {
  def providerConfig: MockProviderConfig
}


trait PactSpec extends FlatSpec with PactBehaviors with HasPactDslProvider with HasProviderConfig {
  /**
   *
   * @param description Test description, gets written into the pact file as the identifier of the test
   * @param model block of code that results in the fixture of given request with expected response
   * @param clientTest Scalatest code that makes the makes the sample request against the provider config mock server
   * @tparam Req Request body type
   * @tparam Res Response body type
   * @return Unit, both Scalatest and Pact( test results to terminal and to a pact json contract) use side effects to complete tests
   */
  def pactSpec[Req: TypeTag, Res](description: String)
    (model: => MockedExchange[Req, Res])
    (clientTest: (MockedExchange[Req, Res], MockProviderConfig) => Any)
    (implicit fpj: PactFormats[Req]) = {
    // Poorly implemented way to pass user code into the pact verification, stub out default val
    val default: ConsumerTestVerification[Any] = (u: Any) => Option.empty[Any]

    val frag = createFragment(description, dslProvider, model)
    val pactResult = frag.duringConsumerSpec(providerConfig)(clientTest(model, providerConfig), default)
    description should behave like pactSuccess(pactResult)
  }

  private def createFragment[Req: TypeTag, Resp](
    description: String,
    builder: PactDslWithProvider,
    mockedExchange: MockedExchange[Req, Resp]
    )(implicit fpj: PactFormats[Req]): PactFragment = {
    val req = mockedExchange.request
    val resp = mockedExchange.response
    val parser = PactFormats.apply[Req]
    println(parser)

    builder
      //      .given("initial state") // TODO allow state as argument
      .uponReceiving(description) // TODO provide description as mocked exchange
      .path(req.path)
      .method(req.httpMethod)
      .headers(new java.util.HashMap[String, String]())
      .body(parser)
      .willRespondWith()
      .status(200)
      .headers(new java.util.HashMap[String, String]()).body(resp.body.toString).toFragment
  }

  val consumerPactBuilder: ConsumerPactBuilder = new ConsumerPactBuilder("mysomething")
  val dslProvider = new PactDslWithProvider(consumerPactBuilder, "theirsomething")

  val pactConfig: PactConfig = PactConfig(PactSpecVersion.V3)

  val providerConfig: MockProviderConfig = MockProviderConfig.createDefault("localhost", pactConfig)
}


//

//  def evaluatePact(pactDslWithProvider1: PactDslWithProvider, consumerConfig1: MockProviderConfig): VerificationResult = {
//    val p: VerificationResult =
//  }


trait PactBehaviors extends Matchers { this: FlatSpec =>

  def pactSuccess(result: VerificationResult) = {
    it should "be a successful pact" in {
      assert(result.isInstanceOf[PactVerified.type])
    }
//    val qq = result should equal(PactVerified)
//    qq

  }

}

/*

	status: 500
	headers: [Access-Control-Allow-Origin:*, Content-Type:application/json, X-Pact-Unexpected-Request:1]
	matchers: null

 */



// TODO LIST !!!!
/*
   1. Nice mapping to the PactDsl.
   2. Mapper from specs2 or scalatest result to ConsumerTestVerification
   3. pass specs2 or scalatest along with the ConsumerTestVerification which takes Test result to an option of itself


 */

//TODO require some type mapper to PactFragment
/**
 * Generate a fragment
 * @param builder
 */