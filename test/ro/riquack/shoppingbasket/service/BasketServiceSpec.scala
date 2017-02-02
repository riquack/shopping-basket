package ro.riquack.shoppingbasket.service

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{MustMatchers, WordSpecLike}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.actors.messages.BasketMessage
import ro.riquack.shoppingbasket.actors.messages.BasketMessage.RevealedContent
import ro.riquack.shoppingbasket.actors.messages.StoreMessage
import ro.riquack.shoppingbasket.models.{Basket, BasketItem}
import ro.riquack.shoppingbasket.services._
import ro.riquack.shoppingbasket.services.responses.BasketServiceError.{InsufficientStockError, MissingItemError}
import ro.riquack.shoppingbasket.services.responses.BasketServiceResponse.{RetrieveSuccess, Success}

class BasketServiceSpec extends TestKit(ActorSystem("store-system"))
  with MockitoSugar
  with TestValues
  with MustMatchers
  with WordSpecLike
  with ScalaFutures {

  import scala.concurrent.ExecutionContext.Implicits.global

  "A BasketService" must {
    "return all the items in the basket" in {
      val basketActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! RevealedContent(defaultBasket) }
      })
      val storeActorRef = mock[ActorRef]
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.list

      result.futureValue mustEqual Right(RetrieveSuccess(Basket(List(basketPhone))))
    }

    "add an item in the basket" in {
      val basketActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! defaultBasket }
      })
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! StoreMessage.Revealed(stockPhone) }
      })
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.add(ItemDTO("ae4cd", 1))

      result.futureValue mustEqual Right(Success)
    }

    "inform that there is not enough stock" in {
      val basketActorRef = mock[ActorRef]
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() !  StoreMessage.ItemInsufficientStock}
      })
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.add(ItemDTO("ae4cd", 1))

      result.futureValue mustEqual Left(InsufficientStockError)
    }

    "inform that the product is not found in the store" in {
      val basketActorRef = mock[ActorRef]
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! StoreMessage.ItemNotFound }
      })
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.add(ItemDTO("ae4cd", 1))

      result.futureValue mustEqual Left(MissingItemError)
    }

    "remove an exiting item from the basket" in {
      val basketActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! BasketMessage.Revealed(BasketItem(phone, 1)) }
      })
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => Unit }
      })
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.remove("ase3a")

      result.futureValue mustEqual Right(Success)
    }

    "inform that the item to be removed is not in the basket" in {
      val basketActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! BasketMessage.ItemNotFound }
      })
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => Unit }
      })
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.remove("ase3a")

      result.futureValue mustEqual Left(MissingItemError)
    }

  }
}
