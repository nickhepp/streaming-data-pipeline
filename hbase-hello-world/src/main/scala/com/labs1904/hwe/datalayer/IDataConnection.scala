package com.labs1904.hwe.datalayer

trait IDataConnection {

  def putString(rowKey: String, columnFamily: String, columnQualifier: String, value: String): Unit

  def putInt(rowKey: String, columnFamily: String, columnQualifier: String, value: Int): Unit

  def putBoolean(rowKey: String, columnFamily: String, columnQualifier: String, value: Boolean): Unit

  def getString(rowKey: String, columnFamily : String, columnQualifier: String) : String

  def getInt(rowKey: String, columnFamily : String, columnQualifier: String) : Int

  def getBoolean(rowKey: String, columnFamily : String, columnQualifier: String) : Boolean

  def getValue[T](rowKey: String,
                  columnFamily: String,
                  columnQualifier: String,
                  bytesToValHandler: (Array[Byte]) => T): T

}
