package com.labs1904.hwe

import com.labs1904.hwe.datalayer.{CaseClassHBaseMapper, IDataConnection}
import org.scalatest.FunSpec
import org.scalatest.mock._
import org.scalatest._
import org.scalamock.scalatest.MockFactory
import org.scalatest.easymock.EasyMockSugar._
import org.scalatest.easymock.EasyMockSugar.mock
import org.easymock.EasyMock.{expect => eexpect, verify => everify, replay => ereplay}

case class TestSerializationClass(
                                   ValInt1: Int,
                                   ValStr2: String,
                                   ValBool3: Boolean
                                 )

class CaseClassHBaseMapperTest extends FunSpec {

  describe("CaseClassHBaseMapper put") {
    it("Tests serializing a case class to an HBase database") {

      // -- arrange
      val rowKey = "rowKey"
      val clmnFam = "clmFam"

      val intClmnQual = "ValInt1"
      val intVal = 3

      val strClmnQual = "ValStr2"
      val strVal = "my-str"

      val boolClmnQual = "ValBool3"
      val boolVal = true

      val mockedDataConn: IDataConnection = mock[IDataConnection]
      expecting{
        eexpect(mockedDataConn.putInt(rowKey, clmnFam, intClmnQual, intVal)).anyTimes()
        eexpect(mockedDataConn.putString(rowKey, clmnFam, strClmnQual, strVal)).anyTimes()
        eexpect(mockedDataConn.putBoolean(rowKey, clmnFam, boolClmnQual, boolVal)).anyTimes()
      }

      val ccMapper = new CaseClassHBaseMapper(mockedDataConn)
      val tsc: TestSerializationClass = TestSerializationClass(intVal, strVal, boolVal)

      // -- act, assert
      whenExecuting(mockedDataConn)
      {
        ccMapper.put[TestSerializationClass](rowKey, clmnFam, tsc)
      }

    }
  }
  
}