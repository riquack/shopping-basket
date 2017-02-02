package ro.riquack.shoppingbasket.actors

import akka.actor.{Actor, ActorLogging}
import ro.riquack.shoppingbasket.actors.messages.BasketMessage._
import ro.riquack.shoppingbasket.models.{Basket, BasketItem}


class BasketActor() extends Actor with ActorLogging {

  private val basket = Basket(List.empty[BasketItem])

  override def preStart(): Unit = log.info("Starting...")

  override def receive: Receive = {

    case RetrieveProducts =>
      sender() ! ShowContent(basket)
      log.info("Retrieved items in the basket...")

    case AddProduct(item, amount) =>
      basket.add(item, amount)
      log.info(s"Added $amount x ${item.id} to basket...")

    case RemoveProduct(id) =>
      basket.find(id) match {
        case Some(basketItem) =>
          basket.remove(basketItem)
          sender() ! Show(basketItem)
          log.info(s"Removed $id from basket...")
        case None =>
          sender() ! ItemNotFound
          log.info(s"Missing $id from basket...")
      }
  }

}
