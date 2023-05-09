package com.labs1904.spark.data

import scala.util.Try

object ReviewParser {

  def parseRawReview(rawReview: String) : Option[Review] = {

    val reviewParts: Array[String] = rawReview.split('\t')
    if (reviewParts.length == 15) {
      val marketplace = reviewParts(0)
      val customer_id: Option[Int] = tryToInt(reviewParts(1))
      val review_id: String = reviewParts(2)
      val product_id: String = reviewParts(3)
      val product_parent: Option[Int] = tryToInt(reviewParts(4))
      val product_title: String = reviewParts(5)
      val product_category: String = reviewParts(6)
      val star_rating: Option[Int] = tryToInt(reviewParts(7))
      val helpful_votes: Option[Int] = tryToInt(reviewParts(8))
      val total_votes: Option[Int] = tryToInt(reviewParts(9))
      val vine: Option[Boolean] = tryToYesNoBoolean(reviewParts(10))
      val verified_purchase: Option[Boolean] = tryToYesNoBoolean(reviewParts(11))
      val review_headline: String = reviewParts(12)
      val review_body: String = reviewParts(13)
      val review_date: Option[ReviewDate] = tryToReviewDate(reviewParts(14))

      val options = Array(customer_id, product_parent, star_rating, helpful_votes, total_votes,
        vine, verified_purchase, review_date)

      if (options.forall(opt => opt.isDefined))
        {
          return Option(
            Review(
              marketplace,
              customer_id.get,
              review_id,
              product_id,
              product_parent.get,
              product_title,
              product_category,
              star_rating.get,
              helpful_votes.get,
              total_votes.get,
              vine.get,
              verified_purchase.get,
              review_headline,
              review_body,
              review_date.get
            )

          )
        }
      else
        {
          return None
        }


    }


    return None
  }

  private def tryToInt( s: String ): Option[Int] = Try(s.toInt).toOption

  private def tryToYesNoBoolean( s: String ): Option[Boolean] = {
    if (s.toLowerCase == "y")
    {
      return Some(true)
    }
    else if (s.toLowerCase() == "n")
    {
      return Some(false)
    }
    else
    {
      return None
    }
  }

  private def tryToReviewDate(s: String) : Option[ReviewDate] = {
    val dateParts = s.split('-')
    if (dateParts.length == 3)
      {
        val year: Option[Int] = tryToInt(dateParts(0))
        val month: Option[Int] = tryToInt(dateParts(1))
        val day: Option[Int] = tryToInt(dateParts(2))
        if (Array(year, month, day).forall(datePart => datePart.isDefined))
          {
            return Option(ReviewDate(year.get, month.get ,day.get))
          }
        else
          {
            return None
          }
      }
    else
      return None
  }


}
