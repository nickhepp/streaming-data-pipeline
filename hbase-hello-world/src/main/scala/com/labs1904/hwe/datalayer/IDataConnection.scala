package com.labs1904.hwe.datalayer

trait IDataConnection {

  def putString(columnFamily: String, rowKey: String, columnQualifier: String, strVal: String): Unit

  def putInt(columnFamily: String, rowKey: String, columnQualifier: String, intVal: Int): Unit

  def putBoolean(columnFamily: String, rowKey: String, columnQualifier: String, boolVal: Boolean): Unit

  def getString(columnFamily : String, rowKey: String, columnQualifier: String) : String

  def getInt(columnFamily : String, rowKey: String, columnQualifier: String) : Int

  def getBoolean(columnFamily : String, rowKey: String, columnQualifier: String) : Boolean

}
