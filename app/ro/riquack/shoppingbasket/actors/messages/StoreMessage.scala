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

  case object ListItems extends StoreInboundMessage

  case class RetrieveItem(id: String) extends StoreInboundMessage

  case class IncrementStock(basketItem: BasketItem) extends StoreInboundMessage

  case class DecrementStock(itemDTO: ItemDTO) extends StoreInboundMessage



  case class Revealed(storeItem: StoreItem) extends StoreOutboundMessage

  case class RevealedContent(store: Store) extends StoreOutboundMessage

  case object AddedStock extends StoreOutboundMessage

  case object ItemNotFound extends StoreOutboundMessage

  case object ItemInsufficientStock extends StoreOutboundMessage

}




