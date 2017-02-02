package ro.riquack.shoppingbasket.actors

import akka.actor.{Actor, ActorLogging}
import akka.event.Logging
import ro.riquack.shoppingbasket.actors.messages.StoreMessage._
import ro.riquack.shoppingbasket.models.{BasketItem, Item, Store}
import ro.riquack.shoppingbasket.repositories.StoreRepository

class StoreActor(storeRepository: StoreRepository) extends Actor with ActorLogging {

  private val store: Store = Store(storeRepository.all)

  override def preStart(): Unit = log.info("Starting...")

  override def receive: Receive = {
    case RetrieveAllProducts =>
      sender() ! ShowContent(store)
      log.info("Listed all items in the store")

    case RetrieveProduct(id) =>
      store.find(id) match {
        case Some(storeItem) =>
          sender() ! Show(storeItem)
          log.info(s"Retrieved item $id")
        case None =>
          sender() ! ItemNotFound
          log.error(s"Could not find $id")
      }

    case DecrementProductStock(reqItem) =>

      val outboundMessage = store.find(reqItem.id) match {
        case Some(storeItem) => {
          if (storeItem.stock >= reqItem.amount) {
            store.removeStock(reqItem)
            log.info(s"Took ${reqItem.amount} x ${reqItem.id} from stock")
            Show(storeItem)
          } else {
            log.info(s"Could not find ${reqItem.amount} x ${reqItem.id} in stock")
            ItemNoStock
          }
        }
        case None =>
          log.error(s"Could not find ${reqItem.id}")
          ItemNotFound
      }
      sender() ! outboundMessage

    case IncrementProductStock(basketItem) =>
      store.addStock(basketItem)
      log.info(s"Added ${basketItem.amount} x ${basketItem.item.id} in stock...")

  }
}
