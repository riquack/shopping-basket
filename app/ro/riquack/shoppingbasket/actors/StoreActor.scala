package ro.riquack.shoppingbasket.actors

import akka.actor.Actor
import akka.event.Logging
import ro.riquack.shoppingbasket.messages.{ItemInStock, ItemNoStock, ItemNotFound}
import ro.riquack.shoppingbasket.messages.StoreMessage._
import ro.riquack.shoppingbasket.models.Item
import ro.riquack.shoppingbasket.repository.StoreRepository

class StoreActor(storeRepository: StoreRepository) extends Actor {

  private val log = Logging(context.system, this)

  private var list: List[Item] = storeRepository.all

  override def preStart(): Unit = log.info("Starting...")

  override def receive: Receive = {
    case RetrieveAllProducts =>
      log.info("Sending list of items in the store...")
      sender() ! list

    case RetrieveProduct(id) =>
      log.info(s"Searching item $id...")
      sender() ! list.find(_.id == id)

    case DecrementProductStock(item) =>
      log.info(s"Taking ${item.amount} x ${item.id} from stock...")

      val stockStatus = list.find(item => item.id == item.id) match {
        case Some(prd) => {
          if (prd.stock >= item.amount) {
            list = list.map(x => if (x == prd) x.copy(stock = x.stock - item.amount) else x)
          ItemInStock(prd)
          } else {
            ItemNoStock
          }
        }
        case None => ItemNotFound
      }
      sender() ! stockStatus

  }
}
