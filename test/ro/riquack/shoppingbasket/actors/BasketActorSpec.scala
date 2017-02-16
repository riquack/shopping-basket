package ro.riquack.shoppingbasket.actors

import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import org.mockito.Mockito.when
import org.mockito.Matchers.any
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}
import ro.riquack.shoppingbasket.{TestKitSugar, TestValues}
import ro.riquack.shoppingbasket.actors.messages.BasketMessage._
import ro.riquack.shoppingbasket.models.Basket
import ro.riquack.shoppingbasket.repositories.BasketRepository

import scala.concurrent.duration._

class BasketActorSpec extends TestKitSugar
  with WordSpecLike
  with MustMatchers
  with ScalaFutures
  with TestValues
  with MockitoSugar {

  implicit val askTimeout = Timeout(2.seconds)
  private val mockBasketRepository = mock[BasketRepository]
  private val basketId = "1s-ve"

  "A BasketActor" must {

    "create a basket in the system" in {
     when(mockBasketRepository.create()) thenReturn basketId
     val basketActorRef = TestActorRef(new BasketActor(mockBasketRepository))
     val result =  (basketActorRef ? CreateBasket).futureValue

     result mustEqual CreatedBasket("1s-ve")
    }

    "send back the items in the basket" in {
      when(mockBasketRepository.retrieve(any[String])) thenReturn Some(defaultBasket)
      val basketActorRef = TestActorRef(new BasketActor(mockBasketRepository))
      val result =  (basketActorRef ? ListProducts(basketId)).futureValue

      result mustEqual RevealedContent(Basket(List(basketPhone)))
    }

    "add an item to the basket" in {
      when(mockBasketRepository.retrieve(any[String])) thenReturn Some(defaultBasket)
      val basketActorRef = TestActorRef(new BasketActor(mockBasketRepository))
      within(100 millis) {
        basketActorRef ! AddProduct(basketId, phone, 1)
        expectNoMsg()
      }
    }

    "remove an item from the basket" in {
      when(mockBasketRepository.retrieve(any[String])) thenReturn Some(defaultBasket)
      val basketActorRef = TestActorRef(new BasketActor(mockBasketRepository))
      basketActorRef ! AddProduct(basketId, phone, 2)
      val result = (basketActorRef ? RemoveProduct(basketId, "ae4cd")).futureValue

      result mustEqual Revealed(basketPhone)
    }

    "inform that an item is not in the basket thus can't be removed" in {
      when(mockBasketRepository.retrieve(any[String])) thenReturn Some(emptyBasket)
      val basketActorRef = TestActorRef(new BasketActor(mockBasketRepository))
      val result = (basketActorRef ? RemoveProduct(basketId, "ae4cd")).futureValue

      result mustEqual ItemNotFound
    }

    "inform that a basket does not exist in the system" in {
      when(mockBasketRepository.retrieve(any[String])) thenReturn None
      val basketActorRef = TestActorRef(new BasketActor(mockBasketRepository))
      val result = (basketActorRef ? ListProducts(basketId)).futureValue

      result mustEqual BasketNotFound
    }

  }
}
