package ro.riquack.shoppingbasket

import akka.actor.{Actor, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}

class TestKitSugar extends TestKit(ActorSystem("store-system")) {

  def testActorRef[A](message: A) = TestActorRef(new Actor {
    override def receive: Receive = { case _ => sender() ! message }
  })

}
