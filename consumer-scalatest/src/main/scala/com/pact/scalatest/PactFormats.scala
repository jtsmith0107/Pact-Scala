package com.pact.scalatest

import au.com.dius.pact.consumer.dsl.{PactDslJsonRootValue, DslPart, PactDslJsonBody}

import scala.reflect.runtime.{universe => ru}
import ru._

/**
 * Type mapper to provide conversions of types to a [[PactDslJsonBody]] for writing contracts for request bodies
 */
trait PactFormats[A] {
  def addTo(builder: PactDslJsonBody, name: String): PactDslJsonBody
}

trait OPactFormats[A] extends PactFormats[A] {

  def asRoot(builder: PactDslJsonBody): DslPart

  /**
   * Default addTo for nested objects
   */
  override def addTo(builder: PactDslJsonBody, name: String): PactDslJsonBody = {
    val openObject = builder.`object`(name)
    val root = asRoot(openObject)
    root.closeObject().asInstanceOf[PactDslJsonBody]
  }
}

object PactFormats {

  def of[A](implicit thing: PactFormats[A]): PactFormats[A] = thing

  def apply[A: TypeTag](implicit fjs: PactFormats[A]): DslPart = {
    val builder = new PactDslJsonBody()
    fjs match {
      case StringPactFormat => PactDslJsonRootValue.stringType()
      case BooleanPactFormat => PactDslJsonRootValue.booleanType()
      case IntPactFormat => PactDslJsonRootValue.integerType()
      case LongPactFormat => PactDslJsonRootValue.integerType()
      case x: OPactFormats[A] => x.asRoot(builder)
      case _ =>
        throw new IllegalStateException()
    }
  }


  // Defaults
  implicit object StringPactFormat extends PactFormats[String] {

    override def addTo(builder: PactDslJsonBody, name: String): PactDslJsonBody = {
      builder.stringType(name)
    }
  }

  implicit object BooleanPactFormat extends PactFormats[Boolean] {

    override def addTo(builder: PactDslJsonBody, name: String): PactDslJsonBody = {
      builder.booleanType(name)
    }
  }

  implicit object IntPactFormat extends PactFormats[Int] {

    override def addTo(builder: PactDslJsonBody, name: String): PactDslJsonBody = {
      builder.integerType(name)
    }
  }

  implicit object LongPactFormat extends PactFormats[Long] {

    override def addTo(builder: PactDslJsonBody, name: String): PactDslJsonBody = {
      builder.integerType(name)
    }
  }

  def usingUnapply[R: TypeTag, A: PactFormats, B: PactFormats](f: R => Option[(A, B)]): OPactFormats[R] = {
    new OPactFormats[R] {

      override def addTo(builder: PactDslJsonBody, name: String): PactDslJsonBody = {
        val openObject = builder.`object`(name)
        val root = asRoot(openObject)
        root.closeObject().asInstanceOf[PactDslJsonBody]
      }

      override def asRoot(builder: PactDslJsonBody): DslPart = {
        val tpe = typeTag[R].tpe
        val paramNames = tpe.members.toIndexedSeq.collect {
          case m: MethodSymbol if m.isCaseAccessor => m.name.toString
        }(collection.breakOut)
        // params seem to get reversed in either the tuple unapply
        val result1 = PactFormats.of[A].addTo(builder, paramNames(1))
        PactFormats.of[B].addTo(result1, paramNames.head)
      }
    }
  }

  def usingUnapply[R: TypeTag, A: PactFormats, B: PactFormats, C: PactFormats](f: R => Option[(A, B, C)]): OPactFormats[R] = {
    new OPactFormats[R] {

      override def asRoot(builder: PactDslJsonBody): DslPart = {
        val tpe = typeTag[R].tpe
        val paramNames = tpe.members.toIndexedSeq.collect {
          case m: MethodSymbol if m.isCaseAccessor => m.name.toString
        }(collection.breakOut)
        // params seem to get reversed in either the tuple unapply
        val result1 = PactFormats.of[A].addTo(builder, paramNames(2))
        val result2 = PactFormats.of[B].addTo(result1, paramNames(1))
        PactFormats.of[C].addTo(result2, paramNames.head)
      }
    }
  }

  // TODO add more tuple OFormats
}

object FlatJsonPactFormats {


  implicit object FlatIntFormat
}

case class PactFormatExcpetion(msg: String = "") extends Exception(msg)