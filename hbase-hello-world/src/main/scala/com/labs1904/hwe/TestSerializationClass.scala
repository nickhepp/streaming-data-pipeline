package com.labs1904.hwe

import scala.annotation.meta.field

case class TestSerializationClass(
                                @(HBaseSerialize @field) ValInt1: Int,
                                ValStr2: String,
                                ValBool3: Boolean

                                 )


