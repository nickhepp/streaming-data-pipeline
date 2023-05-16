package com.labs1904.spark

import com.labs1904.spark.data.{CustomerProfile, Review, ReviewParser, CustomerProfileWithReview, ReviewDate}
import com.labs1904.spark.datalayer.{CaseClassHBaseMapper, HBaseDataConnection}
import org.apache.log4j.Logger
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import com.labs1904.spark.util._
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Table}
import org.apache.spark.sql.catalyst.encoders.RowEncoder

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
        val review: Option[Review] = ReviewParser.parseRawReview(rawReview)
        review
      }): Dataset[Review]

      // compiles
      //val custProfWithReviewsDs: Dataset[Review] = reviews.map(review => review)

      // compiles
//      val custProfWithReviewsDs: Dataset[Review] = reviews.mapPartitions(reviewPartition => {
//        val custProfWithReviews = reviewPartition.map(review => {
//          review
//        }).toList
//        custProfWithReviews.iterator
//      })

      // compiles
//      val custProfWithReviewsDs: Dataset[ReviewDate] = reviews.mapPartitions(reviewPartition => {
//        val custProfWithReviews = reviewPartition.map(review => {
//          ReviewDate(1, 2, 3)
//        }).toList
//        custProfWithReviews.iterator
//      })

      // compiles
//      val custProfWithReviewsDs: Dataset[CustomerProfile] = reviews.mapPartitions(reviewPartition => {
//        val custProfWithReviews = reviewPartition.map(review => {
//          CustomerProfile(
//            "username",
//            "name",
//            "sex",
//            "favorite_color")
//        }).toList
//        custProfWithReviews.iterator
//      })

      // compiles
//      val custProfWithReviewsDs: Dataset[CustomerProfileWithReview] = reviews.mapPartitions(reviewPartition => {
//        val custProfWithReviews = reviewPartition.map(review => {
//          val custProf = CustomerProfile(
//            "username",
//            "name",
//            "sex",
//            "favorite_color")
//          CustomerProfileWithReview(custProf, review)
//        }).toList
//        custProfWithReviews.iterator
//      })

      val custProfWithReviewsDs: Dataset[CustomerProfileWithReview] = reviews.mapPartitions(reviewPartition => {

        val conf = HBaseConfiguration.create()
        conf.set("hbase.zookeeper.quorum", HBaseConnection.HBASE_ZOOKEEPER_QUORUM)
        val connection: Connection = ConnectionFactory.createConnection(conf)
        val table: Table = connection.getTable(TableName.valueOf(HBaseConnection.HBASE_TABLE))
        val hbaseDataConn: HBaseDataConnection = new HBaseDataConnection(table)
        val ccHBaseMapper: CaseClassHBaseMapper = new CaseClassHBaseMapper(hbaseDataConn)

        val custProfWithReviews = reviewPartition.map(review => {
          //val custProf = CustomerProfile(
          //  "username",
          //  "name",
          //  "sex",
          //  "favorite_color")
          val custProf: CustomerProfile = ccHBaseMapper.get[CustomerProfile](
              rowKey = review.customer_id.toString,
              columnFamily = "f1"
              )
          CustomerProfileWithReview(custProf, review)
        }).toList

        connection.close()

        custProfWithReviews.iterator
      })


/*
      val custProfWithReviewsDs = reviews.mapPartitions(reviewPartition => {


        //val conf = HBaseConfiguration.create()
        //conf.set("hbase.zookeeper.quorum", HBaseConnection.HBASE_ZOOKEEPER_QUORUM)
        //var connection: Connection = ConnectionFactory.createConnection(conf)
        //val table: Table = connection.getTable(TableName.valueOf(HBaseConnection.HBASE_TABLE))

        //val hbaseDataConn: HBaseDataConnection = new HBaseDataConnection(table)
        //val ccHBaseMapper: CaseClassHBaseMapper = new CaseClassHBaseMapper(hbaseDataConn)

        //        val custProfWithReviews = reviewPartition.map((review: Review) => {
        //
        //          // read the customer profile
        //          val custProfile: CustomerProfile = ccHBaseMapper.get[CustomerProfile](
        //            rowKey = review.customer_id.toString,
        //            columnFamily = "f1"
        //          )
        //          //return CustomerProfileWithReview(custProfile, review)
        //          return ReviewDate(1, 2, 3)
        //        }).toList

        val custProfWithReviews = reviewPartition.map(review => {
          //ReviewDate(review.customer_id, 2, 3)
          5
        }).toList

        return custProfWithReviews
        //connection.close()

        return custProfWithReviews.iterator

      })
*/

      // transform the logic

      //val result = reviews
      val result = custProfWithReviewsDs

      // Write output to console
      val query = result.writeStream
        .outputMode(OutputMode.Append())
        .format("console")
        .option("truncate", false)
        .trigger(Trigger.ProcessingTime("15 seconds"))
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
