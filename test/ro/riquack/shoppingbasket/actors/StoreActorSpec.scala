package ro.riquack.shoppingbasket.actors

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.actors.messages.StoreMessage._
import ro.riquack.shoppingbasket.actors.messages.StoreOutboundMessage
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.models.{BasketItem, Item, Store}
import ro.riquack.shoppingbasket.repositories.StoreRepository

import scala.concurrent.duration._
import scala.util.Success


class StoreActorSpec extends TestKit(ActorSystem("test-system"))
  with WordSpecLike
  with ScalaFutures
  with MustMatchers
  with TestValues
  with MockitoSugar {

  implicit val askTimeout = Timeout(2.seconds)

  private val mockStoreRepository = mock[StoreRepository]
  when(mockStoreRepository.all) thenReturn List(stockPhone, stockNotebook, stockBike)

  "A StoreActor" must {

    "list all products in the store" in {
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? RetrieveAllProducts).futureValue

      result mustEqual ShowContent(defaultStore)
    }

    "retrieve a products in the store" in {
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? RetrieveProduct("ae4cd")).futureValue

      result mustEqual Show(stockPhone)
    }

    "inform that the product is not in store" in {
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? RetrieveProduct("zz3sd")).futureValue

      result mustEqual ItemNotFound
    }

    "remove item from stock if enough available" in {
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? DecrementProductStock(ItemDTO("ae4cd", 1))).futureValue

      result mustEqual Show(stockPhone)
    }

    "inform that there isn't enough stock for the item" in {
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? DecrementProductStock(ItemDTO("ae4cd", 20))).futureValue

      result mustEqual ItemNoStock
    }

    "inform that there is no such item in store" in {
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? DecrementProductStock(ItemDTO("zz3sd", 1))).futureValue

      result mustEqual ItemNotFound
    }

    "add removed item from basket back in stock" in {
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      within(100 millis) {
        actorActorRef ! IncrementProductStock(BasketItem(phone, 1))
        expectNoMsg()
      }
    }



  }

}
