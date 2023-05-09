package com.labs1904.spark.data

case class Review(marketplace: String,
                  customer_id: Int,
                  review_id: String,
                  product_id: String,
                  product_parent: Int,
                  product_title: String,
                  product_category: String,
                  star_rating: Int,
                  helpful_votes: Int,
                  total_votes: Int,
                  vine: Boolean,
                  verified_purchase: Boolean,
                  review_headline: String,
                  review_body: String,
                  review_date: ReviewDate)
