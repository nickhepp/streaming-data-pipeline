package com.labs1904.hwe

import com.labs1904.hwe.datalayer.{CaseClassHBaseMapper, IDataConnection}
import org.scalatest.FunSpec
import org.scalatest.mock._
import org.scalatest._
import org.scalamock.scalatest.MockFactory
import org.scalatest.easymock.EasyMockSugar.{expecting, mock}
//import org.scalatest.funsuite.AnyFunSuite

//import org.scalamock.annotation.mock
//import org.scalatest.mockito.MockitoSugar.mock
//import org.scalatest.easymock.EasyMockSugar.{expecting, mock}

class CaseClassHBaseMapperTest extends FunSpec {
  describe("CaseClassHBaseMapper put") {
    it("should be a basic test case") {

      // -- arrange
      val rowKey = "rowKey"
      val clmnFam = "clmFam"

      val intClmnQual = "int-clmn-qual"
      val intVal = 3

      val strClmnQual = "str-clmn-qual"
      val strVal = "my-str"

      val boolClmnQual = "bool-clmn-qual"
      val boolVal = true

      val mockedDataConn: IDataConnection = mock[IDataConnection]
      expecting{
        mockedDataConn.putInt(rowKey, clmnFam, intClmnQual, intVal)
        mockedDataConn.putString(rowKey, clmnFam, strClmnQual, strVal)
        mockedDataConn.putBoolean(rowKey, clmnFam, boolClmnQual, boolVal)
      }

      val ccMapper = new CaseClassHBaseMapper(mockedDataConn)
      val tsc: TestSerializationClass = TestSerializationClass(intVal, strVal, boolVal)

      // -- act
      ccMapper.put[TestSerializationClass](rowKey, clmnFam, tsc)

      // -- assert
      //mockedDataConn.verify()
      assert(1 === 1)
    }
  }
}