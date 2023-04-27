package com.labs1904.hwe

//import org.apache.spark.sql.implicits._
import org.apache.spark.sql
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.functions.{col, desc}
import org.apache.spark.sql.types.{DataType, DataTypes, IntegerType, StringType, StructField, StructType}

case class EnrichedUser(ID: Int, name: String, state: String, email: String, multiplied: Int)

object Lecture {

  def printSchemaShow(df: DataFrame) : Unit = {
    df.printSchema()
    df.show
  }

  def printSchemaShowDs(ds: Dataset[EnrichedUser]) : Unit = {
    ds.printSchema()
    ds.show()
  }

  def main(args: Array[String]) : Unit = {

    val idColName = "ID"
    val schema = new StructType().
      add(idColName, IntegerType, nullable = false).
      add("name", StringType, nullable = false).
      add("state", StringType, nullable = false).
      add("email", StringType, nullable = false)

    val spark = SparkSession.builder().appName("HWE Demo").master("local[*]").getOrCreate()
    val df: DataFrame = spark.read.schema(schema).csv("src/main/resources/users.txt")

    import spark.implicits._
    val enrichedUsers: Dataset[EnrichedUser] = df.map(row => EnrichedUser(
      row.getInt(0),
      row.getString(1),
      row.getString(2),
      row.getString(3),
      row.getInt(0) * 3))

    val filterBelow20: Dataset[EnrichedUser] = enrichedUsers.filter(col("multiplied") < 20)
    filterBelow20.printSchema()
    filterBelow20.show()
    printSchemaShowDs(filterBelow20)

    //val multipliedBy3 = df.withColumn("multipliedBy3", col(idColName) * 3)
    //printSchemaShow(multipliedBy3)
  }


}
