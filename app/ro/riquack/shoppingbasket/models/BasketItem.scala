package ro.riquack.shoppingbasket.models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Writes._

case class BasketItem(
    item: Item,
    amount: Int) {
}

object BasketItem {
  implicit val writes: Writes[BasketItem] =
    (
      (__ \ 'id).write[String] and
        (__ \ 'name).write[String] and
        (__ \ 'price).write[BigDecimal] and
        (__ \ 'amount).write[Int])(basketItem => (
      basketItem.item.id,
      s"${basketItem.item.name} by ${basketItem.item.vendor}",
      basketItem.item.price,
      basketItem.amount
    ))
}