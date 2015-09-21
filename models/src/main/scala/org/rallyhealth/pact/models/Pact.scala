package org.rallyhealth.pact.models

import play.api.libs.json.Json

case class Pact(provider: Provider, consumer: Consumer, interactions: Seq[Interaction])

object Pact {

  implicit val format = Json.format[Pact]
}

case class Provider(name: String)

object Provider {

  implicit val format = Json.format[Provider]
}

case class Consumer(name: String)

object Consumer {

  implicit val format = Json.format[Consumer]
}

case class Interaction(
  description: String,
  providerState: Option[String],
  request: PactRequest,
  response: PactResponse
  )

object Interaction {

  implicit val format = Json.format[Interaction]
}

case class PactRequest(
  method: String,
  path: String,
  query: Option[String],
  headers: Option[Map[String, String]],
  body: Option[String]
  )

object PactRequest {

  implicit val format = Json.format[PactRequest]
}

case class PactResponse(
  status: Int = 200,
  headers: Option[Map[String, String]],
  body: Option[String]
  )

object PactResponse {

  implicit val format = Json.format[PactResponse]
}