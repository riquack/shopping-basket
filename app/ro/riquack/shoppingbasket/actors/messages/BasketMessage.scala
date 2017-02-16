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

  case class ListProducts(basketId: String) extends BasketInboundMessage

  case class AddProduct(basketId: String, item: Item, amount: Int) extends BasketInboundMessage

  case class RemoveProduct(basketId: String, id: String) extends BasketInboundMessage

  case object CreateBasket extends BasketInboundMessage

  case class RemoveBasket(basketId: String) extends BasketInboundMessage


  case class Revealed(basketItem: BasketItem) extends BasketOutboundMessage

  case class RevealedContent(basket: Basket) extends BasketOutboundMessage

  case object ItemAdded extends BasketOutboundMessage

  case object ItemNotFound extends BasketOutboundMessage

  case object BasketNotFound extends BasketOutboundMessage

  case class CreatedBasket(id: String)  extends BasketOutboundMessage

  case object RemovedBasket extends BasketOutboundMessage
}


