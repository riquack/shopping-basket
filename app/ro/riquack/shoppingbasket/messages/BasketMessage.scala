package ro.riquack.shoppingbasket.messages

import ro.riquack.shoppingbasket.models.{BasketItem, Item}

/**
  * Type of messages that [[ro.riquack.shoppingbasket.actors.BasketActor]] sends
  */
trait BasketInboundMessage

/**
  * Type of messages that [[ro.riquack.shoppingbasket.actors.BasketActor]] receives
  */
trait BasketOutboundMessage

object BasketMessage {

  case object ListProducts extends BasketInboundMessage

  case class AddProduct(item: Item, amount: Int) extends BasketInboundMessage

  case class RemoveProduct(id: String) extends BasketInboundMessage



  case class ItemRemoved(basketItem: BasketItem) extends BasketOutboundMessage

  case object ItemAdded extends BasketOutboundMessage

  case object ItemNotFound extends BasketOutboundMessage
}


