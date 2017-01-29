package ro.riquack.shoppingbasket.messages

import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.models.{BasketItem, Item}

/**
  * Type of messages that [[ro.riquack.shoppingbasket.actors.StoreActor]] receives
  */
trait StoreInboundMessage

/**
  * Type of messages that [[ro.riquack.shoppingbasket.actors.StoreActor]] sends
  */
trait StoreOutboundMessage

object StoreMessage {

  case object RetrieveAllProducts

  case class RetrieveProduct(id: String)

  case class IncrementProductStock(basketItem: BasketItem)

  case class DecrementProductStock(itemDTO: ItemDTO)

  case class ItemInStock(item: Item) extends StoreOutboundMessage

  case object ItemNotFound extends StoreOutboundMessage

  case object ItemNoStock extends StoreOutboundMessage

  case object AddedStock extends StoreOutboundMessage

}




