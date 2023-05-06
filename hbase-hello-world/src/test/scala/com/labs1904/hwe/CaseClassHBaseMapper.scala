package com.labs1904.hwe

import com.labs1904.hwe.datalayer.IDataConnection

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

class CaseClassHBaseMapper(dataConnection: IDataConnection) {


  def put[TCaseClass <: Product](rowKey: String, columnFamily: String, caseClass: TCaseClass): Unit = {

    val myType = typeOf[TCaseClass]

    // Get the constructor of the class
    val myConstructor = myType.decl(termNames.CONSTRUCTOR).asMethod

    // Get the parameters of the constructor
    val myParams = myConstructor.paramLists.head

    // Iterate over the parameters
    myParams.foreach { param =>
      // Get the name of the parameter
      val paramName = param.name.toString

      // Get the type of the parameter
      val paramType: universe.Type = param.typeSignature

      


      // Print the name and type of the parameter
      println(s"Parameter name: $paramName, type: $paramType")
    }


  }

}
