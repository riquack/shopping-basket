package ro.riquack.shoppingbasket.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask
import ro.riquack.shoppingbasket.messages.ListProducts
import ro.riquack.shoppingbasket.models.Item

import scala.concurrent.{ExecutionContext, Future}

import ro.riquack.shoppingbasket.actors.ActorFactory.defaultTimeout

class BasketService @Inject() (@Named("basketActor") basketActor: ActorRef)(implicit ec: ExecutionContext) {

  def list: Future[List[Item]] = (basketActor ? ListProducts).map(_.asInstanceOf[List[Item]])

}
