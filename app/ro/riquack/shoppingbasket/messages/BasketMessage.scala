package ro.riquack.shoppingbasket.messages

import ro.riquack.shoppingbasket.models.Item

object BasketMessage {

  case object ListProducts

  case class AddProduct(item: Item, amount: Int)

  case class RemoveProduct(item: Item)
}
