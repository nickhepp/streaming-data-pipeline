package com.labs1904.spark

import com.labs1904.spark.data.{CustomerProfile, CustomerProfileWithReview, Review, ReviewDate, ReviewParser}
import com.labs1904.spark.datalayer.{CaseClassHBaseMapper, HBaseDataConnection}
import com.labs1904.spark.util.HdfsConnection._
import com.labs1904.spark.util.KafkaConnection
import com.labs1904.spark.util.HBaseConnection
import org.apache.log4j.Logger
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Table}

/**
 * Spark Structured Streaming app
 *
 */
object StreamingPipeline {
  lazy val logger: Logger = Logger.getLogger(this.getClass)
  val jobName = "StreamingPipeline"

  //Use this for Windows
  val trustStore: String = "src\\main\\resources\\kafka.client.truststore.jks"
  //Use this for Mac
  //val trustStore: String = "src/main/resources/kafka.client.truststore.jks"

  def main(args: Array[String]): Unit = {
    try {
      val spark = SparkSession.builder()
        .config("spark.sql.shuffle.partitions", "3")

        .config("spark.hadoop.dfs.client.use.datanode.hostname", "true")
        .config("spark.hadoop.fs.defaultFS", HDFS_URL)

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


      val custProfWithReviewsDs: Dataset[CustomerProfileWithReview] = reviews.mapPartitions(reviewPartition => {

        val conf = HBaseConfiguration.create()
        conf.set("hbase.zookeeper.quorum", HBaseConnection.HBASE_ZOOKEEPER_QUORUM)
        val connection: Connection = ConnectionFactory.createConnection(conf)
        val table: Table = connection.getTable(TableName.valueOf(HBaseConnection.HBASE_TABLE))
        val hbaseDataConn: HBaseDataConnection = new HBaseDataConnection(table)
        val ccHBaseMapper: CaseClassHBaseMapper = new CaseClassHBaseMapper(hbaseDataConn)

        val custProfWithReviews = reviewPartition.map(review => {
          val custProf: CustomerProfile = ccHBaseMapper.get[CustomerProfile](
              rowKey = review.customer_id.toString,
              columnFamily = "f1"
              )
          CustomerProfileWithReview(custProf, review)
        }).toList

        connection.close()

        custProfWithReviews.iterator
      })

      //val result = reviews
      val result: DataFrame = custProfWithReviewsDs.select("customerProfile.*", "review.*")

      // Write output to HDFS
      val query = result.writeStream
        .outputMode(OutputMode.Append())
        .format("csv")
        .option("delimiter", "\t")
        .option("path", s"/user/${HDFS_USERNAME}/reviews_csv")
        .option("checkpointLocation", s"/user/${HDFS_USERNAME}/reviews_checkpoint")
        .partitionBy("star_rating")
        .trigger(Trigger.ProcessingTime("15 seconds"))
        .start()
      query.awaitTermination()

      // Write output to console
      /*
      val query1 = result.writeStream
        .outputMode(OutputMode.Append())
        .format("console")
        .option("truncate", false)
        .trigger(Trigger.ProcessingTime("5 seconds"))
        .start()
      query1.awaitTermination()
      */


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
