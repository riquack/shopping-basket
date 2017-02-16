package ro.riquack.shoppingbasket.service

import akka.actor.ActorRef
import org.scalatest.{MustMatchers, WordSpecLike}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import ro.riquack.shoppingbasket.{TestKitSugar, TestValues}
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.actors.messages.BasketMessage
import ro.riquack.shoppingbasket.actors.messages.BasketMessage.{ItemAdded, RevealedContent}
import ro.riquack.shoppingbasket.actors.messages.StoreMessage
import ro.riquack.shoppingbasket.models.{Basket, BasketItem}
import ro.riquack.shoppingbasket.services._
import ro.riquack.shoppingbasket.services.responses.BasketServiceError.{InsufficientStockError, MissingItemError}
import ro.riquack.shoppingbasket.services.responses.BasketServiceResponse.{RetrieveSuccess, Success}

class BasketServiceSpec extends TestKitSugar
  with MockitoSugar
  with TestValues
  with MustMatchers
  with WordSpecLike
  with ScalaFutures {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val basketId = "1s-ve"

  "A BasketService" must {
    "return all the items in the basket" in {
      val basketActorRef = testActorRef(RevealedContent(defaultBasket))
      val storeActorRef = mock[ActorRef]
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.list(basketId)

      result.futureValue mustEqual Right(RetrieveSuccess(Basket(List(basketPhone))))
    }

    "add an item in the basket" in {
      val basketActorRef = testActorRef(ItemAdded)
      val storeActorRef = testActorRef(StoreMessage.Revealed(storePhone))
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.add(basketId, ItemDTO("ae4cd", 1))

      result.futureValue mustEqual Right(Success)
    }

    "inform that there is not enough stock" in {
      val basketActorRef = mock[ActorRef]
      val storeActorRef = testActorRef(StoreMessage.ItemInsufficientStock)
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.add(basketId, ItemDTO("ae4cd", 1))

      result.futureValue mustEqual Left(InsufficientStockError)
    }

    "inform that the product is not found in the store" in {
      val basketActorRef = mock[ActorRef]
      val storeActorRef = testActorRef(StoreMessage.ItemNotFound)
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.add(basketId, ItemDTO("ae4cd", 1))

      result.futureValue mustEqual Left(MissingItemError)
    }

    "remove an exiting item from the basket" in {
      val basketActorRef = testActorRef(BasketMessage.Revealed(BasketItem(phone, 1)))
      val storeActorRef = testActorRef(Unit)
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.remove(basketId, "ase3a")

      result.futureValue mustEqual Right(Success)
    }

    "inform that the item to be removed is not in the basket" in {
      val basketActorRef = testActorRef(BasketMessage.ItemNotFound)
      val storeActorRef = testActorRef(Unit)
      val basketService = new BasketService(basketActorRef, storeActorRef)
      val result = basketService.remove(basketId, "ase3a")

      result.futureValue mustEqual Left(MissingItemError)
    }
  }
}
