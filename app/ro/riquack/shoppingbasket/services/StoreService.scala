package ro.riquack.shoppingbasket.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask
import ro.riquack.shoppingbasket.messages.StoreMessage._
import ro.riquack.shoppingbasket.models.{Item, Store}

import scala.concurrent.{ExecutionContext, Future}
import ro.riquack.shoppingbasket.actors.ActorFactory.askTimeout

class StoreService @Inject() (@Named("storeActor") storeActor: ActorRef)(implicit ec: ExecutionContext) {

  def all: Future[Store] = (storeActor ? RetrieveAllProducts).mapTo[Store]

  def find(id: String): Future[Option[Item]] = (storeActor ? RetrieveProduct(id)).map(_.asInstanceOf[Option[Item]])


}
