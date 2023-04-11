package com.labs1904.hwe.exercises

object ChallengeProblems {


  def catsAgeOption(input: Option[Int]): Option[Int] = {

    if (input.isEmpty)
      return None;
    else
      return Some(catsAge(input.get))
  }


  def containsCar(input: List[String]) : List[String] = {
    input.filter(testVal => testVal.contains("car"))
  }


  def sameString(theStr: String) : String = {
    theStr
  }


  def helloWorld() : String = {
    "Hello World!"
  }



  def listSize(list: List[Int]): Int = {
    list.size
  }

  def sumInts(theVal: Int): Int = {
    theVal + 25
  }

  def upper(strList: List[String]): List[String] = {
    strList.map(x => x.toUpperCase())
  }


  def filterNegatives(intList: List[Int]) : List[Int] = {
    intList.filter(testVal => testVal >= 0)
  }

  def sumList(intList: List[Int]) : Int = {
    var sum = 0;
    intList.foreach(x => sum += x);
    return sum;
  }

  def catsAge(humanAge: Int): Int = {
    return humanAge * 4
  }




  /*
  11. Write a function that takes in a list of ints, and return the minimum of the ints provided
  Params - List
  Returns - Int
   */

  /*
  12. Same as question 11, but this time you are given a list of Option[Ints], returns the minimum of the Ints provided.
  If no ints are provided, return None.
 */


  def minimumOption(input: List[Option[Int]]): Option[Int] = {

    val nonNoneOptions: List[Int] = input.filter(inp => !inp.isEmpty).map(x => x.get)
    if (nonNoneOptions.length > 0)
      return Some(nonNoneOptions.min)
    else
      return None

  }


  def minimum(input: List[Int]): Int = {
    input.min
  }


}
