package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.services.StoreService
import ro.riquack.shoppingbasket.services.responses.StoreServiceError._
import ro.riquack.shoppingbasket.services.responses.StoreServiceResponse._

import scala.concurrent.ExecutionContext

class StoreController @Inject () (storeService: StoreService)(implicit ec: ExecutionContext) extends Controller {

  def list = Action.async {
    import ro.riquack.shoppingbasket.models.StoreItem.lightWrites

    storeService.list.map {
      case Right(RetrieveSuccess(items)) => Ok(Json.toJson(items))
      case Left(UnexpectedMessageError) => ControllerMessages.internalServerError
    }

  }

  def retrieve(id: String) = Action.async {
    import ro.riquack.shoppingbasket.models.StoreItem.writes

    storeService.retrieve(id).map {
      case Right(FindSuccess(item)) => Ok(Json.toJson(item))
      case Left(MissingItemError) => ControllerMessages.itemNotFound
      case Left(UnexpectedMessageError) => ControllerMessages.internalServerError
    }
  }
}
