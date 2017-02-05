package ro.riquack.shoppingbasket.actors

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.mockito.Mockito.when
import org.mockito.Matchers.any
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.actors.messages.StoreMessage._
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.repositories.StoreRepository

import scala.concurrent.duration._

class StoreActorSpec extends TestKit(ActorSystem("test-system"))
  with WordSpecLike
  with ScalaFutures
  with MustMatchers
  with TestValues
  with MockitoSugar {

  implicit val askTimeout = Timeout(2.seconds)

  private val mockStoreRepository = mock[StoreRepository]

  "A StoreActor" must {

    "list all products in the store" in {
      when(mockStoreRepository.list()) thenReturn defaultStoreItems
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? ListItems).futureValue

      result mustEqual RevealedContent(defaultStoreItems)
    }

    "retrieve a products in the store" in {
      when(mockStoreRepository.retrieve(any[String])) thenReturn Some(storePhone)
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? RetrieveItem("ae4cd")).futureValue

      result mustEqual Revealed(storePhone)
    }

    "inform that the product is not in store" in {
      when(mockStoreRepository.retrieve(any[String])) thenReturn None
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? RetrieveItem("zz3sd")).futureValue

      result mustEqual ItemNotFound
    }

    "remove item from stock if enough available" in {
      when(mockStoreRepository.retrieve(any[String])) thenReturn Some(storePhone)
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? DecrementStock(ItemDTO("ae4cd", 1))).futureValue

      result mustEqual Revealed(storePhone)
    }

    "inform that there isn't enough stock for the item" in {
      when(mockStoreRepository.retrieve(any[String])) thenReturn Some(storePhone)
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? DecrementStock(ItemDTO("ae4cd", 20))).futureValue

      result mustEqual ItemInsufficientStock
    }

    "inform that there is no such item in store" in {
      when(mockStoreRepository.retrieve(any[String])) thenReturn None
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      val result = (actorActorRef ? DecrementStock(ItemDTO("zz3sd", 1))).futureValue

      result mustEqual ItemNotFound
    }

    "add removed item from basket back in stock" in {
      val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))
      within(100 millis) {
        actorActorRef ! IncrementStock(basketPhone)
        expectNoMsg()
      }
    }



  }

}
