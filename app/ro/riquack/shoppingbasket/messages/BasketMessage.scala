package ro.riquack.shoppingbasket.messages

sealed trait BasketMessage {

}

case object ListProductsMessage extends BasketMessage

case class AddProductMessage() extends BasketMessage

case class RemoveProductMessage() extends BasketMessage
