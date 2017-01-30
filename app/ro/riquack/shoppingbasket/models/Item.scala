package ro.riquack.shoppingbasket.models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Writes._

import scala.math.BigDecimal.RoundingMode

case class Item(
    id: String,
    name: String,
    vendor: String,
    category: String,
    description: String,
    price: BigDecimal,
    reviews: List[Review],
    stock: Int) {

  def rating: Double = {
    val rating = if (reviews.nonEmpty) reviews.map(_.rating).sum.toDouble / reviews.size else 0D
    BigDecimal(rating).setScale(2, RoundingMode.HALF_EVEN).doubleValue()
  }
}

object Item {

  implicit val writes: Writes[Item] = Json.writes[Item]

  implicit val lightWrites: Writes[Item] =
    (
      (__ \ 'id).write[String] and
        (__ \ 'name).write[String] and
        (__ \ 'description).write[String] and
        (__ \ 'price).write[BigDecimal] and
        (__ \ 'rating).write[Double] and
        (__ \ 'inStock).write[Boolean]
      )(item => (
      item.id,
      s"${item.name} by ${item.vendor}",
      s"${item.description.take(10).mkString}...",
      item.price,
      item.rating,
      item.stock > 0
    ))

  implicit val basketWrites: Writes[Item] =
    (
      (__ \ 'id).write[String] and
        (__ \ 'name).write[String] and
        (__ \ 'price).write[BigDecimal]
      )(item => (
      item.id,
      s"${item.name} by ${item.vendor}",
      item.price
    ))
}