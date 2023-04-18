
// Q: What is a typical way that secrets get stored with a project (key vault)?  And would a class like java.util.Properties still get used?

// Q: Can we think of 'Properties' class a dictionary of sorts.  It seemed the KafkaConsumer constructor was expecting to find a huge number of values.

package com.labs1904.hwe.homework

import java.time.Duration
import java.util.{Arrays, Properties, UUID}
import net.liftweb.json.DefaultFormats
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import com.labs1904.hwe.util.Connection._
import org.slf4j.LoggerFactory

object KafkaHomework {
  private val logger = LoggerFactory.getLogger(getClass)

  /**
   * Your task is to try to understand this code and run the consumer successfully. Follow each step below for completion.
   * Implement all the todos below
   */

  //TODO: If these are given in class, change them so that you can run a test. If not, don't worry about this step
  val Topic: String = "question-1"

  implicit val formats: DefaultFormats.type = DefaultFormats

  def main(args: Array[String]): Unit = {

    // Create the KafkaConsumer
    // TODO: Write in a comment what these lines are doing. What are the properties necessary to instantiate a consumer?
    // Create a Properties dictionary for keys that the Kafka consumer is looking for,
    // which includes the group its going to join (random ID in this case), a connection string for the
    // bootstrap server, and deserializers for the keys and values
    val properties = getProperties(BOOTSTRAP_SERVER)
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](properties)


    // TODO: What does this line mean? Write your answer in a comment below
    // Subscribe to the given list of topics to get dynamically assigned partitions.
    consumer.subscribe(Arrays.asList(Topic))

    while (true) {
      // TODO: Change this to be every 5 seconds
      val duration: Duration = Duration.ofSeconds(5)

      //TODO: Look up the ConsumerRecords class below, in your own words what is the class designed to do?
      // Q: Im confused here b/c it almost sounds like a list of a list from the class description,
      // but the coding below makes it look like just a single list.  Also from reading the description
      // I cant tell if it just read one partition.  It does look like it reads just one partition.
      val records: ConsumerRecords[String, String] = consumer.poll(duration)

      records.forEach((record: ConsumerRecord[String, String]) => {
        // Retrieve the message from each record
        //TODO: Describe why we need the .value() at the end of record
        val message = record.value()

        //TODO: If you were given the values for the bootstrap servers in class, run the app with the green play button and make sure it runs successfully. You should see message(s) printing out to the screen
        logger.info(s"Message Received: $message")
      })
    }
  }

  def getProperties(bootstrapServer: String): Properties = {
    // Set Properties to be used for Kafka Consumer

    // the properties class is a persistent set of properties
    val properties = new Properties
    // from browsing into the Consumer cstor, it appears to use these properties to establish a connection
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString)

    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    properties
  }

}
