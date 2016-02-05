package com.pact.scalatest

import com.pact.models.{MockedResponse, MockedRequest, MockedExchange}
import org.scalatest.concurrent.ScalaFutures
import dispatch._, Defaults._

case class Person(name: String, age: Int)

object Person {
  implicit val pactFormats = PactFormats.usingUnapply(unapply _)
}


class PactSampleTest extends PactSpec with ScalaFutures {


  // Only tests that pact is correctly written, needs to incorporate simple client test
  pactSpec("A thing!") {
    val request = MockedRequest("GET", "path/to/cool", Person("bob", 21))
    val response = MockedResponse(200, "dude")
    MockedExchange(request, response)
  } { (fixture, config) =>
    val client = new TestClient(config.url)
    val x = client.makeARequest(fixture.request.path, fixture.request.body)
    whenReady(x) { r =>
      r should contain(fixture.response.body)
    }
  }
}



class TestClient(baseUrl: String) {
  def makeARequest(path: String, body: Person) = {
    val path = "/path/to/cool"
    val reqJson =
      """
        |{
        | "name" : "bob,
        | "age" : 21
        |}
      """.stripMargin
    val svc = url(baseUrl + path).setBody(reqJson).setHeaders(Map.empty).GET
    Http(svc OK as.String)
  }
}