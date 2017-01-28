package ro.riquack.shoppingbasket.messages

sealed trait BasketMessage {

}

case object ListProducts extends BasketMessage

case class AddProduct(id: String) extends BasketMessage

case class RemoveProduct() extends BasketMessage
