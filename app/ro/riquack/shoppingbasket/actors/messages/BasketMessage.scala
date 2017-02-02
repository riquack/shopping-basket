package ro.riquack.shoppingbasket.actors.messages

import ro.riquack.shoppingbasket.models.{Basket, BasketItem, Item}

/**
  * Type of messages that [[ro.riquack.shoppingbasket.actors.BasketActor]] sends
  */
trait BasketInboundMessage

/**
  * Type of messages that [[ro.riquack.shoppingbasket.actors.BasketActor]] receives
  */
trait BasketOutboundMessage

object BasketMessage {

  case object RetrieveProducts extends BasketInboundMessage

  case class AddProduct(item: Item, amount: Int) extends BasketInboundMessage

  case class RemoveProduct(id: String) extends BasketInboundMessage



  case class Show(basketItem: BasketItem) extends BasketOutboundMessage

  case object ItemAdded extends BasketOutboundMessage

  case object ItemNotFound extends BasketOutboundMessage

  case class ShowContent(basket: Basket) extends BasketOutboundMessage
}


