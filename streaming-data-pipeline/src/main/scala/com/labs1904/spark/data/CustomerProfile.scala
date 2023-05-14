package com.labs1904.spark.data

case class CustomerProfile(
                           username: String,
                           name: String,
                           sex: String,
                           favorite_color: String
                          )

/*
-Rowkey: 99
-username: DE - HWE
-name: The Panther
  - sex
: F
  -favorite_color: pink
*/