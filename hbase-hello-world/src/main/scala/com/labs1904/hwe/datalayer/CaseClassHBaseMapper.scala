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

  def get[TCaseClass <: Product](rowKey: String, columnFamily: String)(implicit tt: TypeTag[TCaseClass], ct: ClassTag[TCaseClass], m: Manifest[TCaseClass]): TCaseClass = {

    val fields = scala.reflect.runtime.universe.typeOf[TCaseClass].members.collect {
      case m: scala.reflect.runtime.universe.MethodSymbol if m.isCaseAccessor => m
    }.toList.reverse

    val theArgs: List[Any] = fields.map( field =>
      (field.name.toString, field.returnType.typeSymbol.asType.toType)
    ).map( theVal => {
        var theMatchedVal: Any = null
        theVal._2 match {
          case t if t =:= typeOf[Int] =>
            theMatchedVal = dataConnection.getInt(rowKey, columnFamily, theVal._1).asInstanceOf[Any]
          case t if t =:= typeOf[String] =>
            theMatchedVal = dataConnection.getString(rowKey, columnFamily, theVal._1).asInstanceOf[Any]
          case t if t =:= typeOf[Boolean] =>
            theMatchedVal = dataConnection.getBoolean(rowKey, columnFamily, theVal._1).asInstanceOf[Any]
          case _ =>
            throw new Exception()
            //println(s"${theVal._1} has an unknown type")
        }

        theMatchedVal
      })

    val mirror = scala.reflect.runtime.currentMirror
    val classSymbol = scala.reflect.runtime.universe.typeOf[TCaseClass].typeSymbol.asClass
    val classMirror = mirror.reflectClass(classSymbol)
    val constructorSymbol = scala.reflect.runtime.universe.typeOf[TCaseClass].decl(
      scala.reflect.runtime.universe.termNames.CONSTRUCTOR).asMethod
    val constructorMirror = classMirror.reflectConstructor(constructorSymbol)

    constructorMirror(theArgs: _*).asInstanceOf[TCaseClass]



  }
  /*

  case class MyClass(id:Long,name:String)

  def instantiate[T](classArgs: List[AnyRef])(implicit m : Manifest[T]) : T ={
        val constructor = m.erasure.getConstructors()(0)
        constructor.newInstance(classArgs:_*).asInstanceOf[T]
      }

  val list = List[AnyRef](new java.lang.Long(1),"a name")
  val result = instantiate[MyClass](list)
  println(result.id)



   */



}
