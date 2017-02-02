package ro.riquack.shoppingbasket.models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Writes._

case class Basket(private var items: List[BasketItem]){

  def value = items.foldRight(BigDecimal(0))((cur, acc) => cur.item.price * cur.amount + acc)

  def add(item: Item, amount: Int): Unit = {
    retrieve(item.id) match {
      case Some(basketItem) =>
        remove(basketItem)
        items = items :+ basketItem.copy(amount = basketItem.amount + amount)
      case None => items = items :+ BasketItem(item, amount)
    }
  }

  def remove(basketItem: BasketItem): Unit = items = items.filterNot(_.item.id == basketItem.item.id)

  def retrieve(id: String): Option[BasketItem] = items.find(_.item.id == id)
}


object Basket {
  implicit val writes: Writes[Basket] = (
    (__ \ 'items).write[List[BasketItem]] and
    (__ \ 'value).write[BigDecimal]
  )(basket => (
      basket.items,
      basket.value))
}
