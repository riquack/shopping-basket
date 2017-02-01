package ro.riquack.shoppingbasket.service

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{MustMatchers, WordSpecLike}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.messages.BasketMessage.{ItemNotFound, ItemRemoved}
import ro.riquack.shoppingbasket.messages.StoreMessage
import ro.riquack.shoppingbasket.messages.StoreMessage.{ItemInStock, ItemNoStock}
import ro.riquack.shoppingbasket.models.{Basket, BasketItem}
import ro.riquack.shoppingbasket.services._

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
        override def receive: Receive = { case _ => sender() ! defaultBasket }
      })
      val storeActorRef = mock[ActorRef]

      val basketService = new BasketService(basketActorRef, storeActorRef)

      val result = basketService.list

      result.futureValue mustEqual Basket(List(BasketItem(phone, 1)))
    }

    "add an item in the basket" in {
      val basketActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! defaultBasket }
      })
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! ItemInStock(phone) }
      })

      val basketService = new BasketService(basketActorRef, storeActorRef)

      val result = basketService.add(ItemDTO("ae4cd", 1))

      result.futureValue mustEqual Right(DefaultServiceResponse)
    }

    "inform that there is not enough stock" in {
      val basketActorRef = mock[ActorRef]

      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() !  ItemNoStock}
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
        override def receive: Receive = { case _ => sender() ! ItemRemoved(BasketItem(phone, 1)) }
      })
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => Unit }
      })

      val basketService = new BasketService(basketActorRef, storeActorRef)

      val result = basketService.remove("ase3a")

      result.futureValue mustEqual Right(DefaultServiceResponse)
    }

    "inform that the item to be removed is not in the basket" in {
      val basketActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! ItemNotFound }
      })
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => Unit }
      })

      val basketService = new BasketService(basketActorRef, storeActorRef)

      val result = basketService.remove("ase3a")

      result.futureValue mustEqual Left(DefaultServiceError)
    }

  }
}
