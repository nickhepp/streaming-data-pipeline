package com.labs1904.hwe

import org.scalatest.FunSpec

import scala.reflect.runtime.universe.typeOf
import scala.reflect.runtime.universe._

class HBaseSerializeTests extends FunSpec {
  describe("tests works as expected") {
    it("should be a basic test case") {


      // Get the type of the class
      val myType = typeOf[TestSerialization1Class]

      // Get the constructor of the class
      val myConstructor = myType.decl(termNames.CONSTRUCTOR).asMethod

      // Get the parameters of the constructor
      val myParams = myConstructor.paramLists.head

      // Iterate over the parameters
      myParams.foreach { param =>
        // Get the name of the parameter
        val paramName = param.name.toString

        // Get the type of the parameter
        val paramType = param.typeSignature

        // Print the name and type of the parameter
        println(s"Parameter name: $paramName, type: $paramType")
      }


//      val theAnnotations = typeOf[TestSerializationClass].typeSymbol.asClass.annotations
//
//      val serializeAnnotations = theAnnotations.
//        filter(theAnnot => theAnnot.tree.tpe <:< typeOf[HBaseSerialize])
//
//      val fieldNames = serializeAnnotations.map(annotation =>
//      annotation.tree.symbol.owner.name.toString)
//
//
//      classOf[TestSerializationClass].getAnnotationsByType()


      assert(1 === 1)
    }
  }
}
