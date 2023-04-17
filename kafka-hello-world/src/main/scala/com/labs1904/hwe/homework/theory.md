# Overview

Kafka has many moving pieces, but also has a ton of helpful resources to learn available online. In this homework, your
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
#### What problem does Kafka help solve? Use a specific use case in your answer 
* Helpful resource: [Confluent Motivations and Use Cases](https://youtu.be/BsojaA1XnpM)
* A: Kafka was created as means to handle data in real time.  There was a shift from only handling data as static snapshots, to handling streams of events. One can think of a video game and how the interactions between users and AI opponents need to interact and how those need to happen in real time.  This would be one use case of Kafka.

#### What is Kafka?
* Helpful resource: [Kafka in 6 minutes](https://youtu.be/Ch5VhJzaoaI) 

#### Describe each of the following with an example of how they all fit together: 
 * Topic - a stream of related messages. A topic is a durable log stored on disk.  The hierarchy of topics goes Topics --> Partitions --> Segments
 * Producer - a producer is an entity that pushes data to a Kafka cluster.  Producers are written by the developer.
 * Consumer - a consumer is an entity that reads data from a Kafka cluster.  Consumes are written by the developer.
 * Broker - is a "machine" or compute instance within a cluster.  Each has its own disk.  Brokers are infrastructure which are not written by the developer.
 * Partition - a partition is part of a topic.  Partitioning allows topics to be split across multiple brokers and is how topics can be scaled. Every event in a partition is a strictly ordered piece of the log. 
 * Zookeeper - a zookeeper manages the consensus of distributed state.  There is a proposal to remove zookeepers from Kafka

#### Describe Kafka Producers and Consumers
* A: Producers are entities that produce data, and they are applications that are written by developers.  They push data to Kafka clusters, and clusters are comprised of brokers, and that data is read by consumers.  Like producers, consumers are applications written by developers but they read data from clusters.

#### How are consumers and consumer groups different in Kafka? 
* Helpful resource: [Consumers](https://youtu.be/lAdG16KaHLs)
* Helpful resource: [Confluent Consumer Overview](https://youtu.be/Z9g4jMQwog0)

#### How are Kafka offsets different than partitions? 

#### How is data assigned to a specific partition in Kafka? 

#### Describe immutability - Is data on a Kafka topic immutable? 

#### How is data replicated across brokers in kafka? If you have a replication factor of 3 and 3 brokers, explain how data is spread across brokers
* Helpful resource [Brokers and Replication factors](https://youtu.be/ZOU7PJWZU9w)

#### What was the most fascinating aspect of Kafka to you while learning? 
