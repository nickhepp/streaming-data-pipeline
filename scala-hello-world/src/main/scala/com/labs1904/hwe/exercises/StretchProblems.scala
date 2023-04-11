package com.labs1904.hwe.exercises

import scala.collection.mutable.ListBuffer
import scala.math.Integral.Implicits.infixIntegralOps

object StretchProblems {


  /**
   * Checks if a string is palindrome.
   * @param s string to check for a palindrome
   * @return
   */
  def isPalindrome(s: String): Boolean = {

    // turn the list into a collection
    val chars: List[Char] = s.toList
    // Since the string first half and second half have to match, we are going to find the floor(1/2)
    // the size of the string.  We are doign the floor through integer division since
    // a variable with odd number of characters in length, the middle number equals itself.
    val halfLength: Int = chars.length / 2

    // grab the first half
    val firstHalfChars: List[Char] = chars.take(halfLength)
    // grab the 2nd half and reverse
    val secondHalfChars: List[Char] = chars.takeRight(halfLength).reverse

    // see that the two sides match
    return (firstHalfChars == secondHalfChars)
  }


  /**
   * Given a list of integers, expands them out to a single integer.
   * Elements of the list are ordered such that most significant has the lower element index.
   * So most significant first.
   * @param theInts
   * @return
   */
  def expandValue(theInts: ListBuffer[Int]) : Int = {
    var theVal: Int = 0
    while (theInts.size > 0) {

      val curVal = theInts.remove(0)
      val nextVal = curVal * (scala.math.pow(10, theInts.size)).toInt
      theVal += nextVal
    }

    return theVal
  }

  /**
   * For a given number, return the next largest number that can be created by rearranging that number's digits.
   * If no larger number can be created, return -1
   * @param i Number to retrieve the next biggest number from.
   * @return
   */
  def getNextBiggestNumber(i: Integer): Int = {

    // Decompose the number into its pieces.  Note this would probably be faster
    // if converted to strings so we could avoid division and modulo, but lets do math b/c math is fun.
    var theInts: ListBuffer[Int] = new ListBuffer[Int]()
    var theInt = i;
    while (theInt > 0)
    {
      theInts += (theInt % 10)
      theInt = theInt / 10;
    }

    // now create all the permutations
    val expandedVals: List[Int] = theInts.permutations.map(permu => expandValue(permu)).toList
    val gtrThanExpandedVals : List[Int] = expandedVals.filter(testVal => testVal > i);

    if (gtrThanExpandedVals.size > 0)
      return gtrThanExpandedVals.min
    else
      return -1
  }

}
