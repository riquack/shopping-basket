package ro.riquack.shoppingbasket.messages

sealed trait StoreMessage {

}


case class IncrementProductStock() extends StoreMessage

case class DecrementProductStock() extends StoreMessage

case object RetrieveAllProducts extends StoreMessage

case class RetrieveProduct(id: String) extends StoreMessage


