package ro.riquack.shoppingbasket.actors

import akka.actor.Actor
import akka.event.Logging
import ro.riquack.shoppingbasket.messages.{RetrieveAllProducts, RetrieveProduct}
import ro.riquack.shoppingbasket.models.Item
import ro.riquack.shoppingbasket.repository.StoreRepository

class StoreActor(storeRepository: StoreRepository) extends Actor {

  private val log = Logging(context.system, this)

  private val list: List[Item] = storeRepository.all

  override def preStart(): Unit = log.info("Starting...")

  override def receive: Receive = {
    case RetrieveAllProducts => {
      log.info("Sending list of items in the store...")
      sender() ! list
    }
    case RetrieveProduct(id) => {
      log.info(s"Searching item $id...")
      sender() ! list.find(_.id == id)
    }
  }
}
