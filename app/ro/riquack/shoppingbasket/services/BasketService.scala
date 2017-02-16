package ro.riquack.shoppingbasket.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask

import scala.concurrent.{ExecutionContext, Future}
import ro.riquack.shoppingbasket.actors.ActorFactory.askTimeout
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.actors.messages.BasketMessage._
import ro.riquack.shoppingbasket.actors.messages.StoreMessage
import ro.riquack.shoppingbasket.actors.messages.StoreMessage.{DecrementStock, IncrementStock}
import ro.riquack.shoppingbasket.services.responses.BasketServiceError.{InsufficientStockError, MissingItemError, UnexpectedMessageError, MissingBasketError}
import ro.riquack.shoppingbasket.services.responses.BasketServiceResponse.{CreateSuccess, RetrieveSuccess, Success}
import ro.riquack.shoppingbasket.services.responses.{BasketServiceError, BasketServiceResponse}

class BasketService @Inject() (
    @Named("basketActor") basketActor: ActorRef,
    @Named("storeActor") storeActor: ActorRef)(implicit ec: ExecutionContext) {

  def create: Future[Either[BasketServiceError, BasketServiceResponse]] =
    (basketActor ? CreateBasket).map {
      case CreatedBasket(id) => Right(CreateSuccess(id))
      case _ => Left(UnexpectedMessageError)
    }

  def list(basketId: String): Future[Either[BasketServiceError, BasketServiceResponse]] =
    (basketActor ? ListProducts(basketId)).map {
      case RevealedContent(basket) => Right(RetrieveSuccess(basket))
      case BasketNotFound => Left(MissingBasketError)
      case _ => Left(UnexpectedMessageError)
    }

  def add(basketId: String, item: ItemDTO): Future[Either[BasketServiceError, BasketServiceResponse]] = {
    (storeActor ? DecrementStock(item)).flatMap {
        case StoreMessage.Revealed(storeItem) =>
          (basketActor ? AddProduct(basketId, storeItem.item, item.amount)).map {
            case ItemAdded => Right(Success)
            case BasketNotFound => Left(MissingBasketError)
          }

        case StoreMessage.ItemNotFound => Future.successful(Left(MissingItemError))
        case StoreMessage.ItemInsufficientStock =>  Future.successful(Left(InsufficientStockError))
        case _ => Future.successful(Left(UnexpectedMessageError))
      }
  }

  def remove(basketId: String, itemId: String): Future[Either[BasketServiceError, BasketServiceResponse]] = {
    (basketActor ? RemoveProduct(basketId, itemId)).map {
      case BasketNotFound => Left(MissingBasketError)
      case Revealed(basketItem) => {
        storeActor ! IncrementStock(basketItem)
        Right(Success)
      }
      case ItemNotFound => Left(MissingItemError)
      case _ => Left(UnexpectedMessageError)
    }
  }
}

