package com.labs1904.spark

import com.labs1904.spark.data.{Review, ReviewParser}
import org.apache.log4j.Logger
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import com.labs1904.spark.util._
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Table}

import scala.util.Try

/**
 * Spark Structured Streaming app
 *
 */
object StreamingPipeline {
  lazy val logger: Logger = Logger.getLogger(this.getClass)
  val jobName = "StreamingPipeline"

  val hdfsUrl = "CHANGEME"
  val hdfsUsername = "CHANGEME" // TODO: set this to your handle

  //Use this for Windows
  val trustStore: String = "src\\main\\resources\\kafka.client.truststore.jks"
  //Use this for Mac
  //val trustStore: String = "src/main/resources/kafka.client.truststore.jks"

  def main(args: Array[String]): Unit = {
    try {
      val spark = SparkSession.builder()
        .config("spark.sql.shuffle.partitions", "3")
        .appName(jobName)
        .master("local[*]")
        .getOrCreate()

      import spark.implicits._

      val ds = spark
        .readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", KafkaConnection.BOOTSTRAP_SERVER)
        .option("subscribe", "reviews")
        .option("startingOffsets", "earliest")
        .option("maxOffsetsPerTrigger", "20")
        .option("startingOffsets","earliest")
        .option("kafka.security.protocol", "SASL_SSL")
        .option("kafka.sasl.mechanism", "SCRAM-SHA-512")
        .option("kafka.ssl.truststore.location", KafkaConnection.TRUST_STORE)
        .option("kafka.sasl.jaas.config", getScramAuthString(KafkaConnection.USERNAME, KafkaConnection.PASSWORD))
        .load()
        .selectExpr("CAST(value AS STRING)").as[String]

      // TODO: implement logic here
      val reviews: Dataset[Review] = ds.flatMap((rawReview: String) =>
      {
        val review = ReviewParser.parseRawReview(rawReview)
        review
      }): Dataset[Review]

      reviews.mapPartitions(reviewPartion => {

        val conf = HBaseConfiguration.create()
        conf.set("hbase.zookeeper.quorum", HBaseConnection.HBASE_ZOOKEEPER_QUORUM)
        var connection: Connection = ConnectionFactory.createConnection(conf)
        val table: Table = connection.getTable(TableName.valueOf(HBaseConnection.HBASE_TABLE))


        reviewPartion.map((review: Review) => {
          /*
          - Rowkey: 99
          - username: DE-HWE
          - name: The Panther
          - sex: F
          - favorite_color: pink
           */


          1
        })


        connection.close()

      })


      // transform the logic


      val result = reviews

      // Write output to console
      val query = result.writeStream
        .outputMode(OutputMode.Append())
        .format("console")
        .option("truncate", false)
        .trigger(Trigger.ProcessingTime("5 seconds"))
        .start()

      // Write output to HDFS
//      val query = result.writeStream
//        .outputMode(OutputMode.Append())
//        .format("json")
//        .option("path", s"/user/${hdfsUsername}/reviews_json")
//        .option("checkpointLocation", s"/user/${hdfsUsername}/reviews_checkpoint")
//        .trigger(Trigger.ProcessingTime("5 seconds"))
//        .start()
      query.awaitTermination()
    } catch {
      case e: Exception => logger.error(s"$jobName error in main", e)
    }
  }

  def getScramAuthString(username: String, password: String) = {
    s"""org.apache.kafka.common.security.scram.ScramLoginModule required
   username=\"$username\"
   password=\"$password\";"""
  }
}
