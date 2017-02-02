package ro.riquack.shoppingbasket.models

import scala.math.BigDecimal.RoundingMode

case class Item(
    id: String,
    name: String,
    vendor: String,
    category: String,
    description: String,
    price: BigDecimal,
    reviews: List[Review]) {

  def rating: Double = {
    val rating = if (reviews.nonEmpty) reviews.map(_.rating).sum.toDouble / reviews.size else 0D
    BigDecimal(rating).setScale(2, RoundingMode.HALF_EVEN).doubleValue()
  }
}