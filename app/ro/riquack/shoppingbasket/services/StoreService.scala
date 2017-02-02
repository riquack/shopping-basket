package ro.riquack.shoppingbasket.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import akka.pattern.ask
import ro.riquack.shoppingbasket.actors.messages.StoreMessage._

import scala.concurrent.{ExecutionContext, Future}
import ro.riquack.shoppingbasket.actors.ActorFactory.askTimeout
import ro.riquack.shoppingbasket.services.responses.{StoreServiceError, StoreServiceResponse}
import ro.riquack.shoppingbasket.services.responses.StoreServiceError._
import ro.riquack.shoppingbasket.services.responses.StoreServiceResponse._



class StoreService @Inject() (@Named("storeActor") storeActor: ActorRef)(implicit ec: ExecutionContext) {

  def list: Future[Either[StoreServiceError, StoreServiceResponse]] =
    (storeActor ? ListItems).map {
      case RevealedContent(store) => Right(RetrieveSuccess(store))
      case _ => Left(UnexpectedMessageError)
  }

  def retrieve(id: String): Future[Either[StoreServiceError, StoreServiceResponse]] =
    (storeActor ? RetrieveItem(id)).map {
      case Revealed(storeItem) => Right(FindSuccess(storeItem))
      case ItemNotFound => Left(MissingItemError)
      case _ => Left(UnexpectedMessageError)
    }


}
