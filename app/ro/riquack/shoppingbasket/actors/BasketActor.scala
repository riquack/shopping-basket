package ro.riquack.shoppingbasket.actors

import akka.actor.{Actor, ActorLogging}
import ro.riquack.shoppingbasket.actors.messages.BasketMessage._
import ro.riquack.shoppingbasket.repositories.BasketRepository


class BasketActor(basketRepository: BasketRepository) extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("Starting...")

  override def receive: Receive = {

    case ListProducts(basketId) =>
      basketRepository.retrieve(basketId) match {
        case Some(basket) =>
          sender() ! RevealedContent(basket)
          log.info("Retrieved items in the basket")
        case None =>
          sender() ! BasketNotFound
          log.info("No such basket")
      }

    case AddProduct(basketId, item, amount) =>
      basketRepository.retrieve(basketId) match {
        case Some(_) =>
          basketRepository.addTo(basketId, item, amount)
          log.info(s"Added $amount x ${item.id} to basket")
        case None =>
          sender() ! BasketNotFound
          log.info("No such basket")
      }

    case RemoveProduct(basketId, itemId) =>
      basketRepository.retrieve(basketId) match {
        case Some(basket) =>
          basket.retrieve(itemId) match {
            case Some(basketItem) =>
              basketRepository.removeFrom(basketId, basketItem)
              sender() ! Revealed(basketItem)
            case None =>
              sender() ! ItemNotFound
              log.info(s"Missing $itemId from basket")
          }
        case None =>
          sender() ! BasketNotFound
          log.info("No such basket")
      }

    case CreateBasket =>
      val basketId = basketRepository.create()
      sender() ! CreatedBasket(basketId)
      log.info(s"Created basket $basketId")

    case RemoveBasket(basketId: String) =>
      basketRepository.remove(basketId) match {
        case Some(_) =>
          log.info(s"Removed basket $basketId")
        case None =>
          sender() ! BasketNotFound
          log.info("No such basket")
      }
  }

}
