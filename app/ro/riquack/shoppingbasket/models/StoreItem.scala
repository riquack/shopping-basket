package ro.riquack.shoppingbasket.models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Writes._

case class StoreItem(
    item: Item,
    stock: Int
)

object StoreItem {

  implicit val lightWrites: Writes[StoreItem] =
    (
      (__ \ 'id).write[String] and
      (__ \ 'name).write[String] and
      (__ \ 'description).write[String] and
      (__ \ 'price).write[BigDecimal] and
      (__ \ 'rating).write[Double] and
      (__ \ 'inStock).write[Boolean])(storeItem => (
        storeItem.item.id,
        s"${storeItem.item.name} by ${storeItem.item.vendor}",
        s"${storeItem.item.description.take(10).mkString}...",
        storeItem.item.price,
        storeItem.item.rating,
        storeItem.stock > 0
    ))

  implicit val writes: Writes[StoreItem] =
    (
      (__ \ 'id).write[String] and
        (__ \ 'name).write[String] and
        (__ \ 'vendor).write[String] and
        (__ \ 'description).write[String] and
        (__ \ 'category).write[String] and
        (__ \ 'price).write[BigDecimal] and
        (__ \ 'rating).write[Double] and
        (__ \ 'reviews).write[List[Review]] and
        (__ \ 'stock).write[Int])(storeItem => (
      storeItem.item.id,
      storeItem.item.name,
      storeItem.item.vendor,
      storeItem.item.description,
      storeItem.item.category,
      storeItem.item.price,
      storeItem.item.rating,
      storeItem.item.reviews,
      storeItem.stock
    ))
}
