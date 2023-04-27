package com.labs1904.hwe

import org.apache.log4j.Logger
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._

object WordCountBatchApp {
  lazy val logger: Logger = Logger.getLogger(this.getClass)
  val jobName = "WordCountBatchApp"

  def main(args: Array[String]): Unit = {
    logger.info(s"$jobName starting...")
    try {
      val spark = SparkSession.builder()
        .appName(jobName)
        .config("spark.sql.shuffle.partitions", "3")
        .master("local[*]")
        .getOrCreate()
      import spark.implicits._

      val sentences: Dataset[String] = spark.read.csv("src/main/resources/sentences.txt").as[String]
      //sentences.printSchema
      //sentences.show()

      // TODO: implement me
      val sentencesIntoWords: Dataset[Array[String]] = sentences.map(sentence => splitSentenceIntoWords(sentence))
      //val counts = ???
      //sentencesIntoWords.printSchema()
      //sentencesIntoWords.show()

      val flattenedSentencesIntoWords = sentencesIntoWords.flatMap(item => item)
      //flattenedSentencesIntoWords.printSchema()
      //flattenedSentencesIntoWords.show()

      // Group by item and count the occurrences
      val itemCount = flattenedSentencesIntoWords.groupBy("value").agg(count("value").alias("count")).sort(desc("count"), desc("value"))

      // Show the result
      itemCount.show()

      //counts.foreach(wordCount=>println(wordCount))
    } catch {
      case e: Exception => logger.error(s"$jobName error in main", e)
    }
  }

  // TODO: implement this function
  // HINT: you may have done this before in Scala practice...
  def splitSentenceIntoWords(sentence: String): Array[String] = {
    val expr = "[^a-zA-Z0-9 ]".r // defines a regular expression
    val strippedLine = expr.replaceAllIn(sentence,"")
    strippedLine.split(" ").map(item => item.toLowerCase())
  }

}
