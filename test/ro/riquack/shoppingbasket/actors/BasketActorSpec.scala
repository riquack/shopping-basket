package ro.riquack.shoppingbasket.actors

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{MustMatchers, WordSpecLike}
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.actors.messages.BasketMessage._
import ro.riquack.shoppingbasket.models.Basket

import scala.concurrent.duration._

class BasketActorSpec extends TestKit(ActorSystem("test-store"))
  with WordSpecLike
  with MustMatchers
  with ScalaFutures
  with TestValues {

  implicit val askTimeout = Timeout(2.seconds)

  "A BasketActor" must {

    "send back the items in the basket" in {
      val basketActorRef = TestActorRef[BasketActor]
      val result =  (basketActorRef ? ListProducts).futureValue

      result mustEqual RevealedContent(Basket(List.empty))
    }

    "add an item to the basket" in {
      val basketActorRef = TestActorRef[BasketActor]
      within(100 millis) {
        basketActorRef ! AddProduct(phone, 1)
        expectNoMsg()
      }
    }

    "remove an item from the basket" in {
      val basketActorRef = TestActorRef[BasketActor]
      basketActorRef ! AddProduct(phone, 2)
      val result = (basketActorRef ? RemoveProduct("ae4cd")).futureValue

      result mustEqual Revealed(basketPhone)
    }

    "inform that an item is not in the basket thus can't be removed" in {
      val basketActorRef = TestActorRef[BasketActor]
      val result = (basketActorRef ? RemoveProduct("ae4cd")).futureValue

      result mustEqual ItemNotFound
    }
  }
}
