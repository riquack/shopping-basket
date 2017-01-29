package ro.riquack.shoppingbasket.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask

import scala.concurrent.{ExecutionContext, Future}
import ro.riquack.shoppingbasket.actors.ActorFactory.defaultTimeout
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.messages.BasketMessage.{AddProduct, ListProducts}
import ro.riquack.shoppingbasket.messages.StoreMessage.DecrementProductStock
import ro.riquack.shoppingbasket.messages.{ItemInStock, StoreStockMessage}
import ro.riquack.shoppingbasket.models.Basket

class BasketService @Inject() (
    @Named("basketActor") basketActor: ActorRef,
    @Named("storeActor") storeActor: ActorRef)(implicit ec: ExecutionContext) {

  def list: Future[Basket] = (basketActor ? ListProducts).map(_.asInstanceOf[Basket])

  def add(item: ItemDTO): Future[Either[BasketServiceError, Basket]] = {
    (storeActor ? DecrementProductStock(item))
      .map(_.asInstanceOf[StoreStockMessage])
      .flatMap{
        case ItemInStock(prd) =>
          (basketActor ? AddProduct(prd, item.amount))
            .map(_.asInstanceOf[Basket])
            .map(Right(_))
        case _ => ???
      }

  }

}

trait BasketServiceError
