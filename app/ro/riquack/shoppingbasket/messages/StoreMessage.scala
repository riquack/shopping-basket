package ro.riquack.shoppingbasket.messages

import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.models.Item

object StoreMessage {

  case class IncrementProductStock()

  case class DecrementProductStock(itemDTO: ItemDTO)

  case object RetrieveAllProducts

  case class RetrieveProduct(id: String)

}

sealed trait StoreStockMessage

case class ItemInStock(item: Item) extends StoreStockMessage

case object ItemNotFound extends StoreStockMessage

case object ItemNoStock extends StoreStockMessage




