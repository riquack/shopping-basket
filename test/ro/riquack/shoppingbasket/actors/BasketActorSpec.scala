package ro.riquack.shoppingbasket.actors

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.{MustMatchers, WordSpecLike}
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.messages.BasketMessage._
import ro.riquack.shoppingbasket.messages.BasketOutboundMessage
import ro.riquack.shoppingbasket.models.{Basket, BasketItem}

import scala.concurrent.duration._
import scala.util.Success

class BasketActorSpec extends TestKit(ActorSystem("test-store"))
  with WordSpecLike
  with MustMatchers
  with TestValues {

  implicit val askTimeout = Timeout(2.seconds)

  "A BasketActor" must {

    val basketActorRef = TestActorRef[BasketActor]


    "send back the items in the basket" in {

      val future =  basketActorRef ? ListProducts
      val Success(result: Basket) = future.value.get

      result mustEqual Basket(List.empty)

    }

    "add an item to the basket" in {
      val future = basketActorRef ? AddProduct(phone, 1)
      val Success(result: Basket) = future.value.get

      result mustEqual Basket(List(BasketItem(phone, 1)))
    }

    "remove an item from the basket" in {
      val future = basketActorRef ? RemoveProduct("ae4cd")
      val Success(result: BasketOutboundMessage) = future.value.get

      result mustEqual ItemRemoved(BasketItem(phone, 1))

    }

    "inform that an item is not in the basket thus can't be removed" in {
      val future = basketActorRef ? RemoveProduct("ae4cd")
      val Success(result: BasketOutboundMessage) = future.value.get

      result mustEqual ItemNotFound

    }

  }



}
