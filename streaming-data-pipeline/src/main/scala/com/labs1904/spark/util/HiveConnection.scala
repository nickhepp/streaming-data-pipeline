package com.labs1904.spark.util


/*
The connection class requires creating 2 environment variable before running the app and possibly before starting IntelliJ (see further explanation below).  Please fill the following environment variables with values provided by the HWE admins:

- HWE_HIVE_HDFS_URL
- HWE_HIVE_USERNAME

If the environment variables are not present, an exception will be thrown informing developers
what environment variable(s) are missing.

Note that some environments like Windows need to have IntelliJ restarted before the environment variables will be present in the environment (I haven't tested Linux and Mac).
 */

object HiveConnection {

  private def getConnectionValue(envVarName: String): String = {
    val envVar: Option[String] = sys.env.get(envVarName)
    if (envVar.isEmpty) {
      // Note that if you get this even after creating the environment variable you might need
      // to restart IntelliJ to get the environment variable to be loaded into the process.
      throw new IllegalStateException(s"Environment variable named '$envVarName'" +
        s" was not found. Please create the environment variable named '$envVarName' with" +
        " the values given by the HWE admins.\r\n" +
        "Note you may need to restart the IDE after setting the environment variable.");
    }
    envVar.get
  }

  /** Connection string to the HDFS.
   * This is provided by the HWE admins.
   *
   * @throws IllegalStateException if the environment variable is not found
   */
  @throws(classOf[IllegalStateException])
  val HIVE_HDFS_URL: String = {
    getConnectionValue("HWE_HIVE_HDFS_URL")
  }

  /** Your specific HBase table used in lab.  Value should be:
   * `<first letter of first name><last name>:users`.
   * For example, Kit's table will be `kmenke:users`.
   *
   * @throws IllegalStateException if the environment variable is not found
   */
  @throws(classOf[IllegalStateException])
  val HIVE_USERNAME: String = {
    getConnectionValue("HWE_HIVE_USERNAME")
  }


}
