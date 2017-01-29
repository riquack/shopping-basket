package ro.riquack.shoppingbasket.models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Writes._

case class Basket(var items: List[BasketItem]){

  def value = items.foldRight(BigDecimal(0))((cur, acc) => cur.item.price * cur.amount + acc)

  def add(item: Item, amount: Int): Unit = {
    items.find(_.item.id == item.id) match {
      case Some(_) => items.map {
        case x if x.item == item => x.copy(amount = x.amount + amount)
        case x => x
      }
      case None => items = items :+ BasketItem(item, amount)
    }
  }

  def remove(item: Item): Unit = ???
}


object Basket {
  implicit val writes: Writes[Basket] = (
    (__ \ 'items).write[List[BasketItem]] and
    (__ \ 'value).write[BigDecimal]
  )(basket => (
      basket.items,
      basket.value))
}
