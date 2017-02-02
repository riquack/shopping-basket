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
    case ListItems =>
      sender() ! RevealedContent(store)
      log.info("Listed all items in the store")

    case RetrieveItem(id) =>
      store.retrieve(id) match {
        case Some(storeItem) =>
          sender() ! Revealed(storeItem)
          log.info(s"Retrieved item $id")
        case None =>
          sender() ! ItemNotFound
          log.error(s"Could not find $id")
      }

    case DecrementStock(reqItem) =>

      val outboundMessage = store.retrieve(reqItem.id) match {
        case Some(storeItem) => {
          if (storeItem.stock >= reqItem.amount) {
            store.removeStock(reqItem)
            log.info(s"Took ${reqItem.amount} x ${reqItem.id} from stock")
            Revealed(storeItem)
          } else {
            log.info(s"Could not find ${reqItem.amount} x ${reqItem.id} in stock")
            ItemInsufficientStock
          }
        }
        case None =>
          log.error(s"Could not find ${reqItem.id}")
          ItemNotFound
      }
      sender() ! outboundMessage

    case IncrementStock(basketItem) =>
      store.addStock(basketItem)
      log.info(s"Added ${basketItem.amount} x ${basketItem.item.id} in stock...")

  }
}
