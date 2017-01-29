package ro.riquack.shoppingbasket.models

import play.api.libs.json._


case class BasketItem(
    item: Item,
    amount: Int) {
}

object BasketItem {
  import Item.basketWrites
  implicit val writes: Writes[BasketItem] = Json.writes[BasketItem]
}