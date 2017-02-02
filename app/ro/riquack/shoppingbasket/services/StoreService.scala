package ro.riquack.shoppingbasket.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask
import ro.riquack.shoppingbasket.actors.messages.StoreMessage._

import scala.concurrent.{ExecutionContext, Future}
import ro.riquack.shoppingbasket.actors.ActorFactory.askTimeout
import ro.riquack.shoppingbasket.actors.messages.StoreOutboundMessage
import ro.riquack.shoppingbasket.services.responses.{StoreServiceError, StoreServiceResponse}
import ro.riquack.shoppingbasket.services.responses.StoreServiceError._
import ro.riquack.shoppingbasket.services.responses.StoreServiceResponse._



class StoreService @Inject() (@Named("storeActor") storeActor: ActorRef)(implicit ec: ExecutionContext) {

  def all: Future[Either[StoreServiceError, StoreServiceResponse]] =
    (storeActor ? RetrieveAllProducts).map {
      case ShowContent(store) => Right(RetrieveSuccess(store))
      case _ => Left(UnexpectedMessageError)
  }

  //TODO retrieve instead of find
  def find(id: String): Future[Either[StoreServiceError, StoreServiceResponse]] =
    (storeActor ? RetrieveProduct(id)).map {
      case Show(storeItem) => Right(FindSuccess(storeItem))
      case ItemNotFound => Left(MissingItemError)
      case _ => Left(UnexpectedMessageError)
    }


}
