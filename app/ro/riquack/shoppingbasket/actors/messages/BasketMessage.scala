package ro.riquack.shoppingbasket.actors.messages

import ro.riquack.shoppingbasket.api.dto.ItemDTO
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

  case object ListProducts extends BasketInboundMessage

  case class AddProduct(item: Item, amount: Int) extends BasketInboundMessage

  case class RemoveProduct(id: String) extends BasketInboundMessage



  case class Revealed(basketItem: BasketItem) extends BasketOutboundMessage

  case class RevealedContent(basket: Basket) extends BasketOutboundMessage

  case object ItemAdded extends BasketOutboundMessage

  case object ItemNotFound extends BasketOutboundMessage
}


