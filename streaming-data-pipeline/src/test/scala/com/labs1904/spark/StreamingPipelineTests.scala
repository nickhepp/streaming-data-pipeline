package com.labs1904.spark

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import com.labs1904.spark.util.HdfsConnection
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.scalatest.FunSuite


class StreamingPipelineTests extends FunSuite with DataFrameSuiteBase {
  test("files were written to our user directory") {
    val config = new Configuration()
    config.set("dfs.client.use.datanode.hostname", "true")
    config.set("fs.defaultFS", HdfsConnection.HDFS_URL)
    val filesystem = FileSystem.get(config)
    val fileStatuses = filesystem.listStatus(new Path(s"/user/${HdfsConnection.HDFS_USERNAME}/reviews_json/"))
    fileStatuses.foreach(println)
    assert(fileStatuses.length > 0)
  }
}
