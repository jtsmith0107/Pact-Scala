package com.pact.scalatest

import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import org.scalatest.FlatSpec

class PactTypeMatchersSpec extends FlatSpec {


  case class Name(first: String, last: String)
  object Name {
    implicit lazy val pactFormats = PactFormats.usingUnapply(unapply _)
  }
  case class Person(age: Int, name: Name)
  object Person {

    implicit lazy val pactFormats = PactFormats.usingUnapply(unapply _)
  }

  case class ReversePerson(name: Name, age: Int)
  object ReversePerson {

    implicit lazy val pactFormats = PactFormats.usingUnapply(unapply _)
  }

  case class DoublePerson(people: Person, name: Name)

  object DoublePerson {

    implicit lazy val pactFormats = PactFormats.usingUnapply(unapply _)
  }

//
//  case class Longer(first: String, second: String, third: String)
//
//  object Longer {
//    implicit lazy val pactFormats = PactFormats.usingUnapply(unapply)
//  }

  it should "parse a flat String" in {
    val myPact = new PactDslJsonBody()
    println(PactFormats.StringPactFormat.addTo(myPact, "firstName"))
  }

  it should "parse a flat Int" in {
    val myPact = new PactDslJsonBody()
    println(PactFormats.IntPactFormat.addTo(myPact, "age"))
  }

  it should "parse a flat Boolean" in {
    val myPact = new PactDslJsonBody()
    println(PactFormats.BooleanPactFormat.addTo(myPact, "isCool"))
  }

  it should "parse a flat class structure" in {
    val myPact = new PactDslJsonBody()
    println(Name.pactFormats.asRoot(myPact))
  }

  it should "parse a case class with nested structure" in {
    val myPact = new PactDslJsonBody()
    PactFormats.apply[Person]
  }

  it should "parse a case class the nested structures in separate order" in {
    val myPact = new PactDslJsonBody()
    println(ReversePerson.pactFormats.asRoot(myPact))
  }

  it should "parse a deeply nested class structure" in {
    val myPact = new PactDslJsonBody()
    println(DoublePerson.pactFormats.asRoot(myPact))
  }

//  it should "parse a case class of 3 fields" in {
//    val myPact = new PactDslJsonBody()
//    Longer.pactFormats.asRoot(myPact)
//  }
}
