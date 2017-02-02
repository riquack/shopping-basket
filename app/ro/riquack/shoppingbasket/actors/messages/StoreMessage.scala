package ro.riquack.shoppingbasket.actors.messages

import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.models.{BasketItem, Item, Store, StoreItem}

/**
  * Type of messages that [[ro.riquack.shoppingbasket.actors.StoreActor]] receives
  */
trait StoreInboundMessage

/**
  * Type of messages that [[ro.riquack.shoppingbasket.actors.StoreActor]] sends
  */
trait StoreOutboundMessage

object StoreMessage {

  case object RetrieveAllProducts extends StoreInboundMessage

  case class RetrieveProduct(id: String) extends StoreInboundMessage

  case class IncrementProductStock(basketItem: BasketItem) extends StoreInboundMessage

  case class DecrementProductStock(itemDTO: ItemDTO) extends StoreInboundMessage



  case class Show(storeItem: StoreItem) extends StoreOutboundMessage

  case class ShowContent(store: Store) extends StoreOutboundMessage

  case object ItemNotFound extends StoreOutboundMessage

  case object ItemNoStock extends StoreOutboundMessage

  case object AddedStock extends StoreOutboundMessage

}




