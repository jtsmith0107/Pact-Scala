package com.rallyhealth.authn.client

import java.io.File

import org.rallyhealth.pact.models.Interaction
import org.scalatest.FunSpec
import org.specs2.mutable.Specification
import play.api.Mode.Mode
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.test.PlayRunners
import play.api.{Application, _}
import play.core.server.NettyServer
import play.core.{ApplicationProvider, SourceMapper}
import play.api.libs.json.Json

import scala.concurrent.Future
import scala.util.Success


case class MockedExchange[ReqBody, RespBody](request: MockedRequest[ReqBody], response: MockedResponse[RespBody])

case class MockedRequest[R](httpMethod: String, path: String, body: R)

case class MockedResponse[T](status: Int, body: T)



/**
 * This is a functional builder that takes a Sequence of mocked exchanges to the corresponding partial function
 */
object MockBuilder {

  type MockRoute = PartialFunction[RequestHeader, Handler]

  val defaultRoute: MockRoute = {
    case w => throw new Exception("")
  }

  def setupMocks(fixture: Seq[Interaction): MockRoute = fixture
    .foldRight(defaultRoute) { (
    exchange: MockedExchange[Any, Any],
    pf: MockRoute
    ) =>
    val request = exchange.request
    val mockedResponse = exchange.response

    val requestHandler: MockRoute = {
      case (r: RequestHeader) => // merely takes the type to partial function of to Handler as opposed to just handler, theoretically Action.async converts requestHeader to a request implicitly
        Action.async(BodyParsers.parse.json) { parsed: Request[JsValue] =>
          (parsed.method, parsed.path, parsed.body) match {
            case (request.httpMethod, request.path, request.body) =>
              Future.successful(Results.Status(mockedResponse.status).apply(Json.toJson(mockedResponse.body)).withHeaders(mockedResponse.headers))
          }
        }
    }
    pf orElse requestHandler
  }
}

class PlayMockApplicationProvider(fixture: PartialFunction[RequestHeader, Handler]) extends ApplicationProvider {

  /**
   * Get local application path programmitically
   */
  val applicationPath = new File(".")

  val application = new MockApplication(
    fixture, this.getClass.getClassLoader, new
        MockPlaySettings(fixture)
  )

  Play.start(application)

  def get = Success(application)

  def path = applicationPath

  // TODO Open port discovery and exposure
  val server = new NettyServer(this, Some(7754), mode = application.mode)
}

class MockPlaySettings(
  fixture: PartialFunction[RequestHeader, Handler]
  //  additionalConfiguration: Map[String, _ <: Any] = Map.empty
  ) extends GlobalSettings {

  //  val setupMocks(fixture: Seq[MockedExchange): PartialFunction[RequestHeader, Handler] = fixture.map { exchange =>
  //
  //    val request = exchange.request
  //    val mockedResponse = exchange.response
  //
  //    val requestHandler: PartialFunction[RequestHeader, Handler] = {
  //      case (r: RequestHeader) => // merely takes the type to partial function of to Handler as opposed to just handler, may actually not work
  //        Action.async(BodyParsers.parse.json) { parsed: Request[JsValue] =>
  //           needs body parsers
  //          (parsed.method, parsed.path, parsed.body) match {
  //            case (request.httpMethod, request.path, request.body) =>
  //              Future.successful(Results.Status(mockedResponse.status).apply(mockedResponse.body))
  //          }
  //        }
  //    }
  //    requestHandler
  //  }
  override def onRouteRequest(request: RequestHeader): Option[Handler] = {

    //    val fullPF = setupMocks.fold[PartialFunction[RequestHeader, Handler]]({ case a => throw new Exception("") }) {
    //      (accum, itr) => accum orElse itr
    //    }

    //    fullPF.apply(request)
    Some(fixture.apply(request))
  }

  override def onStart(app: Application) = {
    println(s"Mocked app has started with following mocks $fixture")
  }

  override def onLoadConfig(
    config: Configuration,
    path: File,
    classloader: ClassLoader,
    mode: Mode
    ): Configuration = super
    .onLoadConfig(Configuration.load(new File(path.getAbsolutePath + "conf/mock.conf")), path, classloader, mode)
}

class MockApplication(
  fixture: PartialFunction[RequestHeader, Handler],
  override val classloader: ClassLoader,
  override val global: GlobalSettings,
  override val sources: Option[SourceMapper] = None,
  override val mode: Mode.Mode = Mode.Test,
  override val path: File = new File(".")
  )
  extends Application with WithDefaultPlugins with WithDefaultConfiguration with WithDefaultGlobal

class test extends FunSpec {

  describe("thing" ){
    it("") {
      val appProvider = new PlayMockApplicationProvider(
      { case a => throw new Exception("the partial function exception was thrown") }
      )
      val app = appProvider.application
      running(app) {
        println("The app is indeed running")
        app.configuration.getConfig("application.host")
        Thread.sleep(10000)
      }
      println("Stopping the server")
      appProvider.server.stop()
      success("it worked")
    }
  }
}
