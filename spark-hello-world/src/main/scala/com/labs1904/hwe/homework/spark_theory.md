# Overview

Similar to the work you did for Kafka, this is your crash course into Spark through different questions. In this homework, your
challenge is to write answers that make sense to you, and most importantly, **in your own words!**
Two of the best skills you can get from this class are to find answers to your questions using any means possible, and to
reword confusing descriptions in a way that makes sense to you. 

### Tips
* You don't need to write novels, just write enough that you feel like you've fully answered the question
* Use the helpful resources that we post next to the questions as a starting point, but carve your own path by searching on Google, YouTube, books in a library, etc to get answers!
* We're here if you need us. Reach out anytime if you want to ask deeper questions about a topic 
* This file is a markdown file. We don't expect you to do any fancy markdown, but you're welcome to format however you like
* Spark By Examples is a great resources to start with - [Spark By Examples](https://sparkbyexamples.com/)

### Your Challenge
1. Create a new branch for your answers 
2. Complete all of the questions below by writing your answers under each question
3. Commit your changes and push to your forked repository

## Questions
#### What problem does Spark help solve? Use a specific use case in your answer 
* Helpful resource: [Apache Spark Use Cases](https://www.toptal.com/spark/introduction-to-apache-spark)
* [Overivew of Apache Spark](https://www.youtube.com/watch?v=znBa13Earms&t=42s)
A: Spark solves the problem of processing data at scale by allowing clusters of compute nodes to process data in parallel.  Spark uses RDD, which stands for resilient distributed dataset, to perform this.  RDDs are resilient in that node(s) can die and Spark will handle correcting the error.  Distributed in that multiple nodes are used in a cluster.  DataSet -- the RDD is a logical set of data.

#### What is Apache Spark?
* Helpful resource: [Spark Overview](https://www.youtube.com/watch?v=ymtq8yjmD9I) 
A: A distributed data processing pipeline that works in memory, so its very fast.

#### What is distributed data processing? How does it relate to Apache Spark?  
[Apache Spark for Beginners](https://medium.com/@aristo_alex/apache-spark-for-beginners-d3b3791e259e)

#### On the physical side of a spark cluster, you have a driver and executors. Define each and give an example of how they work together to process data

A: A driver is the application that initiates the work that is spread to executor nodes.  An executor is the computer that is performing the work created by the driver.  Neither the driver nor the cluster manager process the data.  The cluster manager farms out partitions to the executors. 

#### Define each and explain how they are different from each other 
* RDD (Resilient Distributed Dataset)
* DataFrame
* DataSet

#### What is a spark transformation?
[Spark By Examples-Transformations](https://sparkbyexamples.com/apache-spark-rdd/spark-rdd-transformations/)
A: A spark transformation is an operation on spark data that transforms it and provides a new DataFrame/DataSet. Transforms build up a DAG of operations that will be performed when an action is executed to return a result (count values, print data, etc)

#### What is a spark action? How do actions differ from transformations? 
A: A spark action invokes the transformations to be returned, and is an operation on a RDD/DataFrame/DataSet that returns or prints value and does not return another RDD/DataFrame/DataSet.

#### What is a partition in spark? Why would you ever need to repartition? 
[Spark Partitioning](https://sparkbyexamples.com/spark/spark-repartition-vs-coalesce/)

A: A logical partition of data in Spark.  So possibly a couple rows of data.  Spark partitions are not equal to Kafka partitions.

#### What was the most fascinating aspect of Spark to you while learning? 
