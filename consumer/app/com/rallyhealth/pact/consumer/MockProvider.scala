package com.rallyhealth.pact.consumer

import org.rallyhealth.pact.models.{PactResponse, PactRequest}

/**
 * Abstraction for some Mock Service, This leaves definition up to the user, but it would be preferred to use an actual
 * mock service to more fully test http request/response. Alternatively a Mock client could be used which simply uses some
 * mock library like to
 */
sealed trait MockProvider

/**
 * Preferred mocking to mix into your testing, implementations create an actual Http server with a simple controller
 * that handles requests based on the [[PactRequest]] and [[PactResponse]] models. Requires a definition from some
 * server library, which are in turn often paired with json parsing libraries. At the time of writing, choice of json
 * library is a non-trivial task in scala.
 */
trait MockServiceProvider extends MockProvider {

  /**
   * @param request The actual request made by client to be tested
   * @param response The expected http response from the service after the request is made by client.
   * @return Some config of the generated mock server.
   */
  def setupMockCall(request: PactRequest, response: PactResponse): String
}

/**
 * Imitate a service by mocking the client methods directly by some mock library, This is a less complete test of
 * the client/service test interaction and as such the [[MockServiceProvider]] should be preferred. However it may be
 * easier to implement, and would be well decoupled from some server library.
 */
trait MockClientProvider extends MockProvider {

  // TODO possible to generify this??
  def setupMockCall(call: PactRequest => PactResponse): String
}