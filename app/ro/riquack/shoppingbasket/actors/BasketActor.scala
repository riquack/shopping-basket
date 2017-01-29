package ro.riquack.shoppingbasket.actors

import akka.actor.{Actor, ActorLogging}
import ro.riquack.shoppingbasket.messages.BasketMessage._
import ro.riquack.shoppingbasket.models.{Basket, BasketItem}


class BasketActor() extends Actor with ActorLogging {

  private val basket = Basket(List.empty[BasketItem])

  override def preStart(): Unit = log.info("Starting...")

  override def receive: Receive = {

    case ListProducts =>
      log.info("Sending list of items in the basket...")
      sender() ! basket

    case AddProduct(item, amount) =>
      log.info(s"Adding $amount x $item to basket...")
      basket.add(item, amount)
      sender() ! basket

    case RemoveProduct(item) =>
      log.info(s"Removing $item from basket...")
      basket.remove(item)
      sender() ! basket
  }

}
