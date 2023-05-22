# CaseClassHBaseMapper

The `CaseClassHBaseMapper` class automates the mapping of case classes to HBase records, reducing the need for repetitive boilerplate code. It leverages Scala's reflection capabilities to infer field values from case class instances and perform operations to get or put values in HBase.

## Motivation

Mapping data structures to and from HBase records often involves writing repetitive code to extract and store field values. The `CaseClassHBaseMapper` class addresses this issue by automating the mapping process. By defining your data schema using case classes, you can eliminate boilerplate code and achieve a more concise and maintainable solution.

## How It Works

The `CaseClassHBaseMapper` class provides two main methods: `put` and `get`. The `put` method extracts field names and types from the generic type parameter which is of type case class, and field values are extracted from the case class instance.  The combination of name, type, and value for each field provides the schema for the HBase record. 

The `get` method retrieves values from an HBase record and reconstructs a case class instance.  Once again, the case class defines a row schema from the case class field names and types, with the values being extracted from matching columns in the HBase record.

## Usage Example

Here is all the code that is needed to write to an HBase record and read the values back out.

```scala
case class Person(id: Int, name: String, age: Int)

val connection: IDataConnection = // create an instance of IDataConnection

val mapper = new CaseClassHBaseMapper(connection)

val person = Person(1, "John Doe", 30)

// Store the person object in HBase
mapper.put("rowKey1", "columnFamily1", person)

// Retrieve the person object from HBase
val retrievedPerson = mapper.get[Person]("rowKey1", "columnFamily1")

println(retrievedPerson)
```

In the above example, we define a case class Person representing a person's information. We create an instance of CaseClassHBaseMapper by providing an implementation of the IDataConnection interface. Using the put method, we store the person object in HBase with a specific row key and column family. The get method allows us to retrieve the person object based on the same row key and column family.

## Verifying Operation

The `CaseClassHBaseMapper` class depends on the IDataConnection interface which can be an actual HBase table writer in production, or a mock object for unit testing.  

``` scala
trait IDataConnection {

  def putString(rowKey: String, columnFamily: String, columnQualifier: String, value: String): Unit

  def putInt(rowKey: String, columnFamily: String, columnQualifier: String, value: Int): Unit

  def putBoolean(rowKey: String, columnFamily: String, columnQualifier: String, value: Boolean): Unit

  def getString(rowKey: String, columnFamily : String, columnQualifier: String) : String

  def getInt(rowKey: String, columnFamily : String, columnQualifier: String) : Int

  def getBoolean(rowKey: String, columnFamily : String, columnQualifier: String) : Boolean
 
}
```

This project provides unit tests using mock IDataConnection objects for verficaition. 

# Conclusion
The CaseClassHBaseMapper class simplifies mapping case classes to HBase records, reducing boilerplate code and providing a more concise way to interact with HBase.