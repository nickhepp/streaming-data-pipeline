package com.labs1904.hwe.datalayer

import scala.collection.immutable
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

class CaseClassHBaseMapper(dataConnection: IDataConnection) {


  def put[TCaseClass :TypeTag](rowKey: String, columnFamily: String, caseClass: TCaseClass): Unit = {

    val myType: universe.Type = weakTypeOf[TCaseClass]
    val pLists = weakTypeOf[TCaseClass].paramLists

    val x: Product = caseClass.asInstanceOf[Product]


    var i: Int = -1
    val members: Iterable[(String, universe.Type, Any)] = typeOf[TCaseClass].members.collect {
      case m: MethodSymbol if m.isCaseAccessor =>{
        i += 1

        val member: universe.Symbol = myType.member(m.name)

        (m.name.toString, m.returnType, 1)
      }
    }

    members.foreach { case (name, tpe, value) =>
      tpe match {
        case t if t =:= typeOf[Int] =>
          dataConnection.putInt(rowKey, columnFamily, name, value.asInstanceOf[Int])
        case t if t =:= typeOf[String] =>
          dataConnection.putString(rowKey, columnFamily, name, value.asInstanceOf[String])
        case t if t =:= typeOf[Boolean] =>
          dataConnection.putBoolean(rowKey, columnFamily, name, value.asInstanceOf[Boolean])
        case _ =>
          println(s"$name has an unknown type")
      }
    }

  }

}
