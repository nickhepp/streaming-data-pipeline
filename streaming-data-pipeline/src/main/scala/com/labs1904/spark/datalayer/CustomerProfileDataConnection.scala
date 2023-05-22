package com.labs1904.spark.datalayer

import com.labs1904.spark.data.CustomerProfile

class CustomerProfileDataConnection(dataConnection: IDataConnection) {

  def getCustomerProfile(rowKey: String, columnFamily: String): CustomerProfile = {


    val userName = dataConnection.getString(rowKey, columnFamily, "username");
    val name = dataConnection.getString(rowKey, columnFamily, "name");
    val sex = dataConnection.getString(rowKey, columnFamily, "sex");
    val favoriteColor = dataConnection.getString(rowKey, columnFamily, "favorite_color");
    CustomerProfile(userName, name, sex, favoriteColor)
  }

}
