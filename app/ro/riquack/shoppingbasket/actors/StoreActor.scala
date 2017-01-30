package ro.riquack.shoppingbasket.actors

import akka.actor.{Actor, ActorLogging}
import akka.event.Logging
import ro.riquack.shoppingbasket.messages.StoreMessage._
import ro.riquack.shoppingbasket.models.{BasketItem, Item, Store}
import ro.riquack.shoppingbasket.repositories.StoreRepository

class StoreActor(storeRepository: StoreRepository) extends Actor with ActorLogging {

  private val store: Store = Store(storeRepository.all)

  override def preStart(): Unit = log.info("Starting...")

  override def receive: Receive = {
    case RetrieveAllProducts =>
      sender() ! store
      log.info("Listed all items in the store...")

    case RetrieveProduct(id) =>
      sender() ! store.find(id)
      log.info(s"Retrieved item $id...")

    case DecrementProductStock(item) =>

      val stockStatus = store.find(item.id) match {
        case Some(prd) => {
          if (prd.stock >= item.amount) {
            store.removeStock(item)
            ItemInStock(prd)
          } else {
            ItemNoStock
          }
        }
        case None => ItemNotFound
      }
      sender() ! stockStatus
      log.info(s"Took ${item.amount} x ${item.id} from stock...")

    case IncrementProductStock(basketItem) =>
      store.addStock(basketItem)
      log.info(s"Added ${basketItem.amount} x ${basketItem.item.id} in stock...")

  }
}
