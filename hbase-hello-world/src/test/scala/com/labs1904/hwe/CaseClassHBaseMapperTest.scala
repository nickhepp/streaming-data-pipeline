package com.labs1904.hwe

import com.labs1904.hwe.datalayer.{CaseClassHBaseMapper, IDataConnection}
import org.scalatest.FunSpec
import org.scalatest.mock._
import org.scalatest._
import org.scalamock.scalatest.MockFactory
import org.scalatest.easymock.EasyMockSugar._
import org.scalatest.easymock.EasyMockSugar.mock
import org.easymock.EasyMock.{expect => eexpect, replay => ereplay, verify => everify}

import java.util.concurrent.TimeUnit
import scala.collection.mutable.ListBuffer

case class TestSerialization1Class(
                                   ValInt1: Int,
                                   ValStr2: String,
                                   ValBool3: Boolean
                                 )


case class TestSerialization2Class(
                                    MyValBool1: Boolean,
                                    MyValStr2: String,
                                    MyValInt3: Int,
                                    MyValBool4: Boolean,
                                    MyValStr5: String,
                                    MyValInt6: Int)


class CaseClassHBaseMapperTest extends FunSpec {

  def getCurrentMilliseconds(): Long = {
    return TimeUnit.NANOSECONDS.toMillis(System.nanoTime())
  }


  def executeSerialization1Test(): Long = {

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
    expecting {
      eexpect(mockedDataConn.putInt(rowKey, clmnFam, intClmnQual, intVal)).anyTimes()
      eexpect(mockedDataConn.putString(rowKey, clmnFam, strClmnQual, strVal)).anyTimes()
      eexpect(mockedDataConn.putBoolean(rowKey, clmnFam, boolClmnQual, boolVal)).anyTimes()
    }

    val ccMapper = new CaseClassHBaseMapper(mockedDataConn)
    val tsc: TestSerialization1Class = TestSerialization1Class(intVal, strVal, boolVal)

    // -- act, assert
    var timing: Long = 0;
    whenExecuting(mockedDataConn) {
      val startTime: Long = getCurrentMilliseconds()
      ccMapper.put[TestSerialization1Class](rowKey, clmnFam, tsc)
      val endTime: Long = getCurrentMilliseconds()
      timing = (endTime - startTime)

    }

    return timing
  }

  def executeDeserialization1Test(): Long = {
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
    mockedDataConn.getInt(rowKey, clmnFam, intClmnQual).andReturn(intVal)
    mockedDataConn.getString(rowKey, clmnFam, strClmnQual).andReturn(strVal)
    mockedDataConn.getBoolean(rowKey, clmnFam, boolClmnQual).andReturn(boolVal)

    val ccMapper = new CaseClassHBaseMapper(mockedDataConn)

    // -- act
    var timing: Long = 0
    whenExecuting(mockedDataConn) {
      val startTime: Long = getCurrentMilliseconds()
      val item: TestSerialization1Class = ccMapper.get[TestSerialization1Class](rowKey, clmnFam)
      val endTime: Long = getCurrentMilliseconds()
      timing = (endTime - startTime)

      // assert
      assert(item.ValInt1 == intVal)
      assert(item.ValStr2 == strVal);
      assert(item.ValBool3 == boolVal);
    }

    return timing
  }

  ///////////////////// PUTS

  describe("CaseClassHBaseMapper put") {
    it("Tests serializing a case class to an HBase database") {
      val firstTimingInMillisecs: Long = executeSerialization1Test()
      System.out.println(s"Single timing $firstTimingInMillisecs msecs")
    }
  }

  describe("CaseClassHBaseMapper multi-put") {
    it("Tests serializing a case class to an HBase database multiple times") {

      val firstTimingInMillisecs: Long = executeSerialization1Test()

      val timings = new ListBuffer[Long]()
      for (i <- 0 to 9)
        {
          timings += executeSerialization1Test()
        }

      val avgTimingInMillisecs = timings.sum.toDouble / timings.length.toDouble

      System.out.println(s"First timing $firstTimingInMillisecs msecs,\r\n" +
                         s"average follow on timings $avgTimingInMillisecs msecs")

    }
  }


  ///////////////////// GETS

  describe("CaseClassHBaseMapper get") {
    it("Tests deserializing a case class from an HBase database") {
      val firstTimingInMillisecs: Long = executeDeserialization1Test()
      System.out.println(s"Single timing $firstTimingInMillisecs msecs")
    }
  }

  describe("CaseClassHBaseMapper multi-get") {
    it("Tests deserializing a case class from an HBase database multiple times") {

      val firstTimingInMillisecs: Long = executeDeserialization1Test()

      val timings = new ListBuffer[Long]()
      for (i <- 0 to 9) {
        timings += executeDeserialization1Test()
      }

      val avgTimingInMillisecs = timings.sum.toDouble / timings.length.toDouble

      System.out.println(s"First timing $firstTimingInMillisecs msecs,\r\n" +
        s"average follow on timings $avgTimingInMillisecs msecs")

    }
  }
}