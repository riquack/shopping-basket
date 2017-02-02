package ro.riquack.shoppingbasket.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask

import scala.concurrent.{ExecutionContext, Future}
import ro.riquack.shoppingbasket.actors.ActorFactory.askTimeout
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.actors.messages.BasketMessage._
import ro.riquack.shoppingbasket.actors.messages.StoreMessage
import ro.riquack.shoppingbasket.actors.messages.StoreMessage.{DecrementProductStock, IncrementProductStock}
import ro.riquack.shoppingbasket.services.responses.BasketServiceError.{InsufficientStockError, MissingItemError, UnexpectedMessageError}
import ro.riquack.shoppingbasket.services.responses.BasketServiceResponse.{RetrieveSuccess, Success}
import ro.riquack.shoppingbasket.services.responses.{BasketServiceError, BasketServiceResponse}

class BasketService @Inject() (
    @Named("basketActor") basketActor: ActorRef,
    @Named("storeActor") storeActor: ActorRef)(implicit ec: ExecutionContext) {


  def list: Future[Either[BasketServiceError, BasketServiceResponse]] =
    (basketActor ? RetrieveProducts).map {
      case ShowContent(basket) => Right(RetrieveSuccess(basket))
      case _ => Left(UnexpectedMessageError)
    }

  def add(item: ItemDTO): Future[Either[BasketServiceError, BasketServiceResponse]] = {
    (storeActor ? DecrementProductStock(item)).map {
        case StoreMessage.Show(storeItem) =>
          basketActor ! AddProduct(storeItem.item, item.amount)
            Right(Success)
        case StoreMessage.ItemNotFound => Left(MissingItemError)
        case StoreMessage.ItemNoStock =>  Left(InsufficientStockError)
        case _ => Left(UnexpectedMessageError)
      }
  }

  def remove(id: String): Future[Either[BasketServiceError, BasketServiceResponse]] = {
    (basketActor ? RemoveProduct(id)).map {
      case Show(basketItem) => {
        storeActor ! IncrementProductStock(basketItem)
        Right(Success)
      }
      case ItemNotFound => Left(MissingItemError)
      case _ => Left(UnexpectedMessageError)
    }
  }
}

