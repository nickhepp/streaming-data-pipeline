package com.labs1904.hwe

import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Get, Put, Result, Table}
import org.apache.hadoop.hbase.client.{Scan}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.logging.log4j.{LogManager, Logger}
import com.labs1904.hwe.util.HBaseConnection._


object MyApp {
  lazy val logger: Logger = LogManager.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    logger.info("MyApp starting...")
    var connection: Connection = null
    try {

      val exeChallenge1 = false
      val exeChallenge2 = false
      val exeChallenge3 = false
      val exeChallenge4 = true

      val family = Bytes.toBytes("f1");
      val mailClmn: Array[Byte] = Bytes.toBytes("mail")

      val usernameClmn = Bytes.toBytes("username")
      val nameClmn = Bytes.toBytes("name")
      val sexClmn = Bytes.toBytes("sex")
      val favoriteColorClmn = Bytes.toBytes("favorite_color")

      val ninetyNineRowKey = "99"

      val conf = HBaseConfiguration.create()
      conf.set("hbase.zookeeper.quorum", HBASE_ZOOKEEPER_QUORUM)
      connection = ConnectionFactory.createConnection(conf)
      val table: Table = connection.getTable(TableName.valueOf("nheppermann:users"))

      // CHALLENGE #1: read data from existing user
      if (exeChallenge1)
        {
          val get = new Get(Bytes.toBytes("10000001"))
          val clmnGet = get.addColumn(family, mailClmn)
          val result: Result = table.get(clmnGet)
          val emailAddress = Bytes.toString(result.getValue(family, mailClmn))
          logger.debug(emailAddress)
        }

      // CHALLENGE #2: Write a new user to your table
      if (exeChallenge2)
        {

          performPut(table, ninetyNineRowKey, family, usernameClmn, "DE-HWE")
          performPut(table, ninetyNineRowKey, family, nameClmn, "The Panther")
          performPut(table, ninetyNineRowKey, family, sexClmn, "F")
          performPut(table, ninetyNineRowKey, family, favoriteColorClmn, "pink")
        }

      // CHALLENGE #3: How many user IDs exist between 10000001 and 10006001
      if (exeChallenge3)
      {
        val startRow = Bytes.toBytes("10000001")
        val stopRow = Bytes.toBytes("10006001")
        val rowKeyScan: Scan = new Scan().withStartRow(startRow).withStopRow(stopRow)

        val resultScanner = table.getScanner(rowKeyScan)
        // Reading values from scan result
        // TODO: change resultScanner.ForEach
        var count: Int = 0
        var result: Result = resultScanner.next
        while (result != null) {
          count += 1
          result = resultScanner.next
        }
        logger.debug(s"Found rows  $count")
      }

      // Challenge #4: Delete the user with ID = 99
      if (exeChallenge4)
      {
        val delete = new Delete()
      }


    } catch {
      case e: Exception => logger.error("Error in main", e)
    } finally {
      if (connection != null) connection.close()
    }
  }

  def performPut(table: Table,
                 rowKey: String,
                 columnFamily: Array[Byte],
                 columnQualifier: Array[Byte],
                 value: String
                ) : Unit = {
    val put = new Put(Bytes.toBytes(rowKey))
    put.addColumn(columnFamily, columnQualifier, Bytes.toBytes(value));
    table.put(put)

    logGet(table, rowKey, columnFamily, columnQualifier)
  }

  def logGet(table: Table,
             rowKey: String,
             columnFamily: Array[Byte],
             columnQualifier: Array[Byte]
    ) : Unit =
  {
    val get = new Get(Bytes.toBytes(rowKey))
    val clmnGet = get.addColumn(columnFamily, columnQualifier)
    val result: Result = table.get(clmnGet)
    logger.debug(Bytes.toString(result.getValue(columnFamily, columnQualifier)))
  }

}
