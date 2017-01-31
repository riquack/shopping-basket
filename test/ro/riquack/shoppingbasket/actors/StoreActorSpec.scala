package ro.riquack.shoppingbasket.actors

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.messages.StoreMessage._
import ro.riquack.shoppingbasket.messages.StoreOutboundMessage
import ro.riquack.shoppingbasket.models.{BasketItem, Item, Store}
import ro.riquack.shoppingbasket.repositories.StoreRepository

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success


class StoreActorSpec extends TestKit(ActorSystem("test-system"))
  with WordSpecLike
  with MustMatchers
  with TestValues
  with MockitoSugar {

  implicit val askTimeout = Timeout(2.seconds)

  private val mockStoreRepository = mock[StoreRepository]
  when(mockStoreRepository.all) thenReturn List(phone, notebook, bike)

  "A StoreActor" must {

    val actorActorRef = TestActorRef(new StoreActor(mockStoreRepository))

    "list all products in the store" in {
      val future = actorActorRef ? RetrieveAllProducts
      val Success(result: Store) = future.value.get

      result mustEqual defaultStore
    }

    "retrieve a products in the store" in {
      val future = actorActorRef ? RetrieveProduct("ae4cd")
      val Success(Some(result: Item)) = future.value.get

      result mustEqual phone
    }

    "inform that the product is not in store" in {
      val future = actorActorRef ? RetrieveProduct("zz3sd")
      val Success(result) = future.value.get

      result mustEqual None
    }

    "remove item from stock if enough available" in {
      val future = actorActorRef ? DecrementProductStock(ItemDTO("ae4cd", 1))
      val Success(result: StoreOutboundMessage) = future.value.get

      result mustEqual ItemInStock(phone)

    }

    "inform that there isn't enough stock for the item" in {
      val future = actorActorRef ? DecrementProductStock(ItemDTO("ae4cd", 20))
      val Success(result: StoreOutboundMessage) = future.value.get

      result mustEqual ItemNoStock

    }

    "inform that there is no such item in store" in {
      val future = actorActorRef ? DecrementProductStock(ItemDTO("zz3sd", 1))
      val Success(result: StoreOutboundMessage) = future.value.get

      result mustEqual ItemNotFound

    }

    "add removed item from basket back in stock" in {
      within(100 millis) {
        actorActorRef ! IncrementProductStock(BasketItem(phone, 1))
        expectNoMsg()
      }
    }



  }

}
