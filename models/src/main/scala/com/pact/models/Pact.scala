package com.pact.models

import scala.language.existentials

/**
 * Complete json model for a pact file
 * @param provider Provider versioning info
 * @param consumer Consumer versioning info
 * @param interactions List of interactions that are generated and tested against.
 */
case class Pact(provider: Provider, consumer: Consumer, interactions: Seq[Interaction], metaData: MetaData)

case class Provider(name: String)

case class Consumer(name: String)

/**
 * The basic building block of a pact. Each interaction represents a single test in the consumer dsl, and translates
 * @param providerState Signals to the provider to setup specific state before an interaction needs to be tested.
 * @param description Test title to be tracked between provider/consumer
 * @param request Real request for consumer, mocked request for provider test
 * @param response Mocked response for consumer test. Actual service response for provider test.
 */
case class Interaction(
  providerState: Option[String],
  description: String,
  request: PactRequest[_],
  response: PactResponse[_]
  )

/**
 * Full representation of HTTP request.
 */
case class PactRequest[Body](
  method: String,
  path: String,
  query: Option[String],
  headers: Map[String, String],
  body: Body
  )

/**
 * Full representation of HTTP response
 */
case class PactResponse[Body](
  status: Int = 200,
  headers: Map[String, String],
  body: Body
  )

/**
 * Contains data on the specific pact library used to publish the pact
 */
case class MetaData(pactScalaVersion: String)