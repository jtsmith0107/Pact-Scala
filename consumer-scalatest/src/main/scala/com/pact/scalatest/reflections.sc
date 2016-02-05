import au.com.dius.pact.consumer.dsl.{DslPart, PactDslJsonArray, PactDslJsonBody}
import com.pact.scalatest.PactFormats
import scala.reflect.runtime.{universe => ru}
import ru._


case class Name(first: String, last: String)
object Name {
  implicit lazy val thing = PactFormats.usingUnapply(unapply)
}
case class Person(first: Name, age: String)
object Person {

  implicit lazy val thing = PactFormats.usingUnapply(unapply)
}

val myW = new PactDslJsonBody()
PactFormats.StringPactFormat.addTo(myW, "name")
Person.thing.asRoot(myW)
//val rm = runtimeMirror(getClass.getClassLoader)
//val werw = new PactDslJsonBody()
//  .stringType("name")
//  .`object`("info")
//  .booleanType("cool")
//  .stringType("age")
//  .closeObject()
//  .`object`("moreInfo!")
//  .`object`("heyo")
//  .integerType("wat")
//  .closeObject()
//  .closeObject()
////
////
//
//
//
//
//// implicit json writer to
val w2 = new PactDslJsonBody()
  .stringType("name")
  .booleanType("cool")
  .`object`("info")
  .stringType("age")
  .closeObject()
  .`object`("moreInfo!")
  .`object`("heyo")
  .integerType("wat")
  .closeObject()
  .closeObject()
////
////val w3 = new PactDslJsonBody()
////  .stringType("name")
////  .`object`("info")
////  .booleanType("cool")
////  .stringType("age")
////  .closeObject()
////  .`object`("moreInfo!")
////  .stringType("extraCoolVal")
////  .closeObject()
////  .`object`("heyo")
////  .integerType("wat")
////  .closeObject()
////  .`object`("moreInfo!") // Overrides the previous moreInfo!
////  .stringType("WAT?!")
////  .closeObject()
////
////val w4 = new PactDslJsonBody()
////  .eachLike("wat")
////.stringType("hasstuff")
////.closeObject()
////.closeArray()
////
////val w5 = new PactDslJsonArray()
////  .stringType("yo")
////
////
////
////
////
////
////
////werw.toString
////w2.toString
////w3.toString
////w4.toString
////w5.toString
/////*
//
//
//val y: Any = Name("he", "be")
//val c = y.getClass
//
//
////def caseMap[T: TypeTag: reflect.ClassTag](instance: T): List[(String, Any)] = {
////  val im = rm.reflect(instance)
////  typeOf[T].members.collect {
////
////    case m: MethodSymbol if m.isCaseAccessor =>
////      val name  = m.name.toString
////      val value = im.reflectMethod(m).apply()
////      println(s"foud type $name, $value, $im")
////      (name, value)
////
////  } (collection.breakOut)
////}
//
//
//import scala.reflect.runtime.universe._
//
///**
// * Returns a map from formal parameter names to types, containing one
// * mapping for each constructor argument.  The resulting map (a ListMap)
// * preserves the order of the primary constructor's parameter list.
// */
//def caseClassParamsOf[T: TypeTag]: ListMap[String, Type] = {
//  val tpe = typeOf[T]
//  println(tpe)
//  val constructorSymbol = tpe.decl(termNames.CONSTRUCTOR)
//  val defaultConstructor =
//    if (constructorSymbol.isMethod) constructorSymbol.asMethod
//    else {
//      val ctors = constructorSymbol.asTerm.alternatives
//      ctors.map { _.asMethod }.find { _.isPrimaryConstructor }.get
//    }
//
//  ListMap[String, Type]() ++ defaultConstructor.paramLists.reduceLeft(_ ++ _).map {
//    sym => sym.name.toString -> tpe.member(sym.name).asMethod.returnType
//  }
//}
//
//
//
//
////def caseMap[T: TypeTag](instance: Class[T]): List[(String, reflect.runtime.universe.Type)] = {
//// typeOf[T].members.filter(!_.isMethod).map(x => (x.name.toString, x.typeSignature)).toList
////}
//val name = Name("heyo", "heyo")
//val sample =Person(name, "10")
//def x[T: TypeTag]: PactDslJsonBody => PactDslJsonBody = {
//  val members = caseClassParamsOf[T]
//
//  val result: List[PactDslJsonBody => PactDslJsonBody] = members.map { case (name, p) =>
//    p match {
//      case thing if thing =:= typeOf[Boolean] =>
//        { q: PactDslJsonBody => q.booleanType(name) }
//      case thing if thing =:= typeOf[Int] =>
//        { q: PactDslJsonBody => q.numberType(name) }
//      case thing if thing =:= typeOf[String] =>
//        { q: PactDslJsonBody => q.stringType(name) }
//      case other =>
//        // Nested currently unsupported
//        throw new IllegalStateException("does not support nested class structures")
//
////     Check for nested structure, evaluate fields and flatten as necessary
////      case other: Type =>
////
////        val objectVals = { q: PactDslJsonBody => q.`object`(name) } :: x[] ::
////          { q: PactDslJsonBody => q.closeObject().asInstanceOf[PactDslJsonBody] } :: Nil
////        println(s"have object vals $objectVals")
////        flattenPactBuilder(objectVals.reverse)
//    }
//  }.toList
//  result match {
//    case Nil => x => x
//    case some => flattenPactBuilder(some)
//  }
//}
//def flattenPactBuilder(ops: List[PactDslJsonBody => PactDslJsonBody]): PactDslJsonBody => PactDslJsonBody = {
//    ops.reduceRight{_ compose _}
//}
////val kskdf= "hi"
//val pweprpwer = x[Name].apply(new PactDslJsonBody())
//println("lkjaslkdjfasd")
////
////val ppp = x.foldLeft(new PactDslJsonBody()){
////  case (acc, next: (PactDslJsonBody => PactDslJsonBody)) => next(acc _)
////}
//// Map a nested recurvise json structure to a linear sequence of builder methods
//// "closing" an object is final, so all elements of an object need to be known at once.