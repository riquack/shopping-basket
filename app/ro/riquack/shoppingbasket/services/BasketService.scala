package ro.riquack.shoppingbasket.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask

import scala.concurrent.{ExecutionContext, Future}
import ro.riquack.shoppingbasket.actors.ActorFactory.askTimeout
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.messages.BasketMessage._
import ro.riquack.shoppingbasket.messages.StoreMessage.{DecrementProductStock, IncrementProductStock, ItemInStock}
import ro.riquack.shoppingbasket.messages._
import ro.riquack.shoppingbasket.models.Basket

class BasketService @Inject() (
    @Named("basketActor") basketActor: ActorRef,
    @Named("storeActor") storeActor: ActorRef)(implicit ec: ExecutionContext) {


  def list: Future[Basket] = (basketActor ? ListProducts).map(_.asInstanceOf[Basket])

  def add(item: ItemDTO): Future[Either[BasketServiceError, BasketServiceResponse]] = {
    (storeActor ? DecrementProductStock(item))
      .mapTo[StoreOutboundMessage]
      .flatMap {
        case ItemInStock(prd) =>
          (basketActor ? AddProduct(prd, item.amount))
            .mapTo[Basket]
            .map(_ => Right(DefaultServiceResponse))
        case StoreMessage.ItemNotFound => Future.successful(Left(MissingItemError))
        case StoreMessage.ItemNoStock =>  Future.successful(Left(InsufficientStockError))
      }
  }

  def remove(id: String): Future[Either[BasketServiceError, BasketServiceResponse]] = {
    (basketActor ? RemoveProduct(id)).mapTo[BasketOutboundMessage]
      .map {
        case ItemRemoved(basketItem) => {
          storeActor ! IncrementProductStock(basketItem)
          Right(DefaultServiceResponse)
        }
        case ItemNotFound => Left(DefaultServiceError)
      }
  }

}

trait BasketServiceError

object DefaultServiceError extends BasketServiceError
object InsufficientStockError extends BasketServiceError
object MissingItemError extends BasketServiceError


trait BasketServiceResponse

object DefaultServiceResponse extends BasketServiceResponse

