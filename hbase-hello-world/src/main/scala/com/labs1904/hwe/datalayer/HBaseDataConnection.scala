package com.labs1904.hwe.datalayer

import org.apache.hadoop.hbase.client.{Get, Put, Result, Table}
import org.apache.hadoop.hbase.util.Bytes

class HBaseDataConnection(table: Table) extends IDataConnection {


  private def getValue[T](rowKey: String,
                  columnFamily: String,
                  columnQualifier: String,
                  bytesToValHandler: (Array[Byte]) => T): T = {
    val get: Get = new Get(Bytes.toBytes(rowKey))
    val columnFamilyBytes = Bytes.toBytes(columnFamily)
    val columnQualifierBytes = Bytes.toBytes(columnQualifier)
    val clmnGet = get.addColumn(columnFamilyBytes, columnQualifierBytes)
    val result: Result = table.get(clmnGet)
    val resultBytes: Array[Byte] = result.getValue(columnFamilyBytes, columnQualifierBytes)
    bytesToValHandler(resultBytes)
  }



  override def getString(rowKey: String, columnFamily: String, columnQualifier: String): String = {
    getValue[String](
      rowKey,
      columnFamily,
      columnQualifier,
      resultBytes => Bytes.toString(resultBytes))
  }

  override def getInt(columnFamily: String, rowKey: String, columnQualifier: String): Int = {
    getValue[Int](
      rowKey,
      columnFamily,
      columnQualifier,
      resultBytes => Bytes.toInt(resultBytes))
  }

  override def getBoolean(columnFamily: String, rowKey: String, columnQualifier: String): Boolean = {
    getValue[Boolean](
      rowKey,
      columnFamily,
      columnQualifier,
      resultBytes => Bytes.toBoolean(resultBytes))
  }

  private def putValue[T](rowKey: String,
                          columnFamily: String,
                          columnQualifier: String,
                          value: T,
                          bytesToValHandler: (T) => Array[Byte]): Unit = {
    val put: Put = new Put(Bytes.toBytes(rowKey))
    val columnFamilyBytes = Bytes.toBytes(columnFamily)
    val columnQualifierBytes = Bytes.toBytes(columnQualifier)


    put.addColumn(columnFamilyBytes, columnQualifierBytes, Bytes.toBytes(value));
    val clmnGet = get.addColumn(columnFamilyBytes, columnQualifierBytes)
    val result: Result = table.get(clmnGet)
    val resultBytes: Array[Byte] = result.getValue(columnFamilyBytes, columnQualifierBytes)
    bytesToValHandler(resultBytes)
  }


  override def putInt(columnFamily: String, rowKey: String, columnQualifier: String, intVal: Int): Unit = ???

  override def putString(columnFamily: String, rowKey: String, columnQualifier: String, strVal: String): Unit = ???

  override def putBoolean(columnFamily: String, rowKey: String, columnQualifier: String, boolVal: Boolean): Unit = ???
}
