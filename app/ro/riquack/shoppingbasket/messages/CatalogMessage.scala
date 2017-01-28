package ro.riquack.shoppingbasket.messages

sealed trait CatalogMessage {

}


case class AddStockMessage() extends CatalogMessage

case class RemoveStockMessage() extends CatalogMessage
