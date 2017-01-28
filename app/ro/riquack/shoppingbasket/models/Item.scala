package ro.riquack.shoppingbasket.models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Writes._

case class Item(
    id: String,
    name: String,
    vendor: String,
    category: String,
    description: String,
    price: BigDecimal,
    reviews: List[Review],
    stock: Int) {

  def rating: Long = if (reviews.nonEmpty) reviews.foldRight(0)(_.rating + _) / reviews.size else 0

}

object Item {

  implicit val lightWrites: Writes[Item] =
    (
      (__ \ 'id).write[String] and
        (__ \ 'name).write[String] and
        (__ \ 'description).write[String] and
        (__ \ 'price).write[BigDecimal] and
        (__ \ 'rating).write[Long] and
        (__ \ 'inStock).write[Boolean]
      )(item => (
      item.id,
      s"${item.name} by ${item.vendor}",
      s"${item.description.take(10).mkString}...",
      item.price,
      item.rating,
      item.stock > 0
    ))

  implicit val writes = Json.writes[Item]

}