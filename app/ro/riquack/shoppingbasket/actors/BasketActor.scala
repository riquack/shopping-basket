package ro.riquack.shoppingbasket.actors

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import ro.riquack.shoppingbasket.messages.{AddProduct, ListProducts}
import ro.riquack.shoppingbasket.models.Item

class BasketActor(storeActor: ActorRef) extends Actor {

  private val log = Logging(context.system, this)

  private val items: List[Item] = List.empty

  override def preStart(): Unit = log.info("Starting...")

  override def receive: Receive = {
    case ListProducts => {
      log.info("Sending list of items in the basket...")
      sender() ! items
    }
    case AddProduct(id) => {
      log.info(s"Adding $id to basket...")
      ???
    }
  }

}
