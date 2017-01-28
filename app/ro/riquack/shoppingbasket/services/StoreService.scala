package ro.riquack.shoppingbasket.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask
import ro.riquack.shoppingbasket.messages.{RetrieveAllProducts, RetrieveProduct}
import ro.riquack.shoppingbasket.models.Item

import scala.concurrent.{ExecutionContext, Future}
import ro.riquack.shoppingbasket.actors.ActorFactory.defaultTimeout

class StoreService @Inject() (@Named("storeActor") storeActor: ActorRef)(implicit ec: ExecutionContext) {

  def all: Future[List[Item]] = (storeActor ? RetrieveAllProducts).map(_.asInstanceOf[List[Item]])

  def find(id: String): Future[Option[Item]] = (storeActor ? RetrieveProduct(id)).map(_.asInstanceOf[Option[Item]])


}
