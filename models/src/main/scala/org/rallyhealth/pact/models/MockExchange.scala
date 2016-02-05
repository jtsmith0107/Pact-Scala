package org.rallyhealth.pact.models

/**
 * Abstraction for a full request response cycle that we expect to happen during a client test
 */
case class MockedExchange[ReqBody, RespBody](request: MockedRequest[ReqBody], response: MockedResponse[RespBody])

case class MockedRequest[R](httpMethod: String, path: String, body: R, headers: Map[String, String] = Map.empty)

case class MockedResponse[T](status: Int, body: T, headers: Map[String, String] = Map.empty)