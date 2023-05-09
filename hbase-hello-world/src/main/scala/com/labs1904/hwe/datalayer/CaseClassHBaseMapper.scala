package com.labs1904.hwe.datalayer

import scala.reflect.ClassTag
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe.{TypeTag, typeOf}

class CaseClassHBaseMapper(dataConnection: IDataConnection) {



  private def getMemberInfos[TCaseClass](a: TCaseClass)(implicit tt: TypeTag[TCaseClass], ct: ClassTag[TCaseClass]): Iterable[(String, universe.Type, Any)] = {
    val members = tt.tpe.members.collect {
      case m if m.isMethod && m.asMethod.isCaseAccessor => m.asMethod
    }
    val memberInfos: Iterable[(String, universe.Type, Any)] = members.map { member =>
      val memberValue: Any = tt.mirror.reflect(a).reflectMethod(member)()
      (member.name.toString, member.returnType, memberValue)
    }
    memberInfos
  }


  def put[TCaseClass <: Product](rowKey: String, columnFamily: String, caseClass: TCaseClass)(implicit tt: TypeTag[TCaseClass], ct: ClassTag[TCaseClass]): Unit = {

    val members: Iterable[(String, universe.Type, Any)] = getMemberInfos(caseClass)

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
