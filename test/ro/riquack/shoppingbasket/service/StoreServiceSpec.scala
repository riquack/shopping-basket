package ro.riquack.shoppingbasket.service

import akka.actor.{Actor, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{MustMatchers, WordSpecLike}
import org.scalatest.mockito.MockitoSugar
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.actors.messages.StoreMessage._
import ro.riquack.shoppingbasket.models.Store
import ro.riquack.shoppingbasket.services.StoreService
import ro.riquack.shoppingbasket.services.responses.StoreServiceError.MissingItemError
import ro.riquack.shoppingbasket.services.responses.StoreServiceResponse.{FindSuccess, RetrieveSuccess}


class StoreServiceSpec extends TestKit(ActorSystem("store-system"))
  with MockitoSugar
  with TestValues
  with MustMatchers
  with WordSpecLike with ScalaFutures {

  import scala.concurrent.ExecutionContext.Implicits.global

  "A StoreService" must {
    "return all items in the store" in {
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! RevealedContent(defaultStore) }
      })

      val storeService = new StoreService(storeActorRef)

      val result = storeService.list

      result.futureValue mustEqual Right(RetrieveSuccess(Store(List(stockPhone, stockNotebook, stockBike))))
    }

    "return a requested item in the store" in {
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! Revealed(stockPhone) }
      })

      val storeService = new StoreService(storeActorRef)

      val result = storeService.retrieve("ae4cd")

      result.futureValue mustEqual Right(FindSuccess(stockPhone))
    }

    "not return an item that is not the store" in {
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! ItemNotFound }
      })

      val storeService = new StoreService(storeActorRef)

      val result = storeService.retrieve("ae4cd")

      result.futureValue mustEqual Left(MissingItemError)
    }
  }

}
