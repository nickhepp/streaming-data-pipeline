# Overview

By now you've seen some different Big Data frameworks such as Kafka and Spark. Now we'll be focusing in on HBase. In this homework, your
challenge is to write answers that make sense to you, and most importantly, **in your own words!**
Two of the best skills you can get from this class are to find answers to your questions using any means possible, and to
reword confusing descriptions in a way that makes sense to you. 

### Tips
* You don't need to write novels, just write enough that you feel like you've fully answered the question
* Use the helpful resources that we post next to the questions as a starting point, but carve your own path by searching on Google, YouTube, books in a library, etc to get answers!
* We're here if you need us. Reach out anytime if you want to ask deeper questions about a topic 
* This file is a markdown file. We don't expect you to do any fancy markdown, but you're welcome to format however you like


### Your Challenge
1. Create a new branch for your answers 
2. Complete all of the questions below by writing your answers under each question
3. Commit your changes and push to your forked repository

## Questions
#### What is a NoSQL database? 
A: A NoSQL database is basically every other type of data base that is not a relational database.  Often the data is a key-value pair or the NoSQL database will have tables but the tables cannot be joined and the table structures are not near as rigid as with relation databases.  NoSQL systems are also horizontally scalable meaning that they can be scaled by adding more computers, this is opposed to SQL databases which are vertically scalable by adding more RAM and cores to the single computer.  NoSQL databases trade ACID compliance for speed.

#### In your own words, what is Apache HBase? 
A: HBase is a distributed and scalable big data store that is based on Google's design of Bigtable.  It's a NoSQL database.

#### What are some strengths and limitations of HBase? 
* [HBase By Examples](https://sparkbyexamples.com/apache-hbase-tutorial/)
A: Strengths - it can be scaled to handle massive amounts of data.  It can store about any type of data.  Limitations - data can't be joined.


#### Explain the following concepts: 
* Rowkey - a rowkey is a way to look up data by a unique indentifier.
* Column Qualifier - Essentially the name of a column.
* Column Family - Set of columns physically stored together on a single disk.

Table --> Rowkey --> Column Family --> Column Qualifier/Name

#### What are the differences between Get and Put commands in HBase? 
* [HBase commands](https://www.tutorialspoint.com/hbase/hbase_create_data.htm)

Get - The get command and the get() method of HTable class are used to read data from a table in HBase.
Put - Using put command, you can insert rows into a table.

#### What is the HBase Scan command for? 
* [HBase Scan](https://www.tutorialspoint.com/hbase/hbase_scan.htm)
The scan command is used to view the data in HTable.

#### What was the most interesting aspect of HBase when went through all the questions? 