package ro.riquack.shoppingbasket.actors

import akka.actor.{Actor, ActorLogging}
import ro.riquack.shoppingbasket.messages.BasketMessage._
import ro.riquack.shoppingbasket.models.{Basket, BasketItem}


class BasketActor() extends Actor with ActorLogging {

  private val basket = Basket(List.empty[BasketItem])

  override def preStart(): Unit = log.info("Starting...")

  override def receive: Receive = {

    case ListProducts =>
      sender() ! basket
      log.info("Retrieved items in the basket...")

    case AddProduct(item, amount) =>
      basket.add(item, amount)
      sender() ! basket
      log.info(s"Added $amount x ${item.id} to basket...")

    case RemoveProduct(id) =>
      basket.find(id) match {
        case Some(basketItem) =>
          basket.remove(basketItem)
          sender() ! ItemRemoved(basketItem)
        case None =>
          sender() ! ItemNotFound
      }
      log.info(s"Removed $id from basket...")
  }

}
