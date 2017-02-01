package ro.riquack.shoppingbasket.service

import akka.actor.{Actor, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{MustMatchers, WordSpecLike}
import org.scalatest.mockito.MockitoSugar
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.models.Store
import ro.riquack.shoppingbasket.services.StoreService


class StoreServiceSpec extends TestKit(ActorSystem("store-system"))
  with MockitoSugar
  with TestValues
  with MustMatchers
  with WordSpecLike with ScalaFutures {

  import scala.concurrent.ExecutionContext.Implicits.global

  "A StoreService" must {
    "return all items in the store" in {
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! defaultStore }
      })

      val storeService = new StoreService(storeActorRef)

      val result = storeService.all

      result.futureValue mustEqual Store(List(phone, notebook, bike))
    }

    "return a requested item in the store" in {
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! Some(phone) }
      })

      val storeService = new StoreService(storeActorRef)

      val result = storeService.find("ae4cd")

      result.futureValue mustEqual Some(phone)
    }

    "not return an item that is not the store" in {
      val storeActorRef = TestActorRef(new Actor {
        override def receive: Receive = { case _ => sender() ! None }
      })

      val storeService = new StoreService(storeActorRef)

      val result = storeService.find("ae4cd")

      result.futureValue mustEqual None
    }
  }

}
