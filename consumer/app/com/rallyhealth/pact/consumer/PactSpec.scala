package com.rallyhealth.pact.consumer

import java.io.{File, PrintWriter}

import org.rallyhealth.pact.models._
import org.scalatest._

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

trait PactSpec extends FlatSpec with BeforeAndAfterAll with BuildInfoProvider with JsonBodyParser with BeforeAndAfterEach{

  def pactName: String

    val pactFile = new PrintWriter(new File(filePath))

  /* Tempted to pull in shapeless' HList here or a monoid or some other variable to hold mutable state.
  Mutable state here would make it difficult to ensure any order to the test results that are added to a pact. This may
  not be a problem normally, but provides a hard stipulation to any test writers that tests MUST not use state between
  tests or race conditions could come into affect.

  Futher investigation into plugging into scalatest deeper could be an option. We could add side affects to any test report generators, to write our pact files
  along side the test reporting.

  It may be possible to use classTags and reflections cleverly to "recover" the type at runtime and then provide Bodyparsers implicitly
  */

  def withInteraction[Req: ClassTag, Resp: ClassTag](pactMock: Interaction[Req, Resp])(testCode: Interaction[Req, Resp] => Any) = {
    pactInteractions += pactMock
    testCode(pactMock)
  }

  // TODO define own ScalaTest ItWords??


  override protected def beforeAll() = {

  }

  // Write Pact when done
  override protected def afterEach() = {
    //    val metaVersion = buildinfo.BuildInfo.version
    val fullPact = Pact(Provider(providerVersion), Consumer(consumerVersion), pactInteractions.result(), MetaData(metaVersion))
    val filePath = s"$pactName-${fullPact.consumer.name}-${fullPact.provider.name}.json"

    pactFile.append(jsonToStringParser(fullPact))
  }
  // Write Pact when done
  override protected def afterAll() = {
//    val metaVersion = buildinfo.BuildInfo.version
    val metaVersion = "tempStub"
    val fullPact = Pact(Provider(providerVersion), Consumer(consumerVersion), pactInteractions.result(), MetaData(metaVersion))
    val filePath = s"$pactName-${fullPact.consumer.name}-${fullPact.provider.name}.json"

    val pactFile = new PrintWriter(new File(filePath))
    pactFile.write(jsonToStringParser(fullPact))
    pactFile.close()
  }
}

