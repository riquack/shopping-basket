package ro.riquack.shoppingbasket.actors

import java.util.UUID
import javax.inject.Inject

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.duration._
import ro.riquack.shoppingbasket.repository.StoreRepository

class ActorFactory @Inject()(actorSystem: ActorSystem) {

  def storeActor = actorSystem.actorOf(Props(classOf[StoreActor], new StoreRepository), s"store-${UUID.randomUUID()}")

  def basketActor = actorSystem.actorOf(Props(classOf[BasketActor]), s"basket-${UUID.randomUUID()}")

}

object ActorFactory {

  implicit val defaultTimeout = Timeout(2.seconds)

  val systemName = "shopping-store"

}
