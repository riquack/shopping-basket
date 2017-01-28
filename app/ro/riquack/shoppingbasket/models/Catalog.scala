package ro.riquack.shoppingbasket.models

import akka.actor.Actor
import akka.actor.Actor.Receive

class Catalog extends Actor {

  val list: List[Item] = CatalogGenerator.generate

  override def receive: Receive = ???
}
