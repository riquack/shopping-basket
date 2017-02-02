package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.api.dto.ErrorDTO
import ro.riquack.shoppingbasket.services.StoreService
import ro.riquack.shoppingbasket.services.responses.StoreServiceError.{MissingItemError, UnexpectedMessageError}
import ro.riquack.shoppingbasket.services.responses.StoreServiceResponse.{FindSuccess, RetrieveSuccess}

import scala.concurrent.ExecutionContext

class StoreController @Inject () (storeService: StoreService)(implicit ec: ExecutionContext) extends Controller {

  def list = Action.async {
    import ro.riquack.shoppingbasket.models.Store.writes
    import ErrorDTO.writes

    storeService.all.map {
      case Right(RetrieveSuccess(items)) => Ok(Json.toJson(items))
      case Left(UnexpectedMessageError) => InternalServerError(Json.toJson(ErrorDTO("An unexpected error occurred")))
    }

  }

  //TODO rename find to retrieve
  def find(id: String) = Action.async {
    import ro.riquack.shoppingbasket.models.StoreItem.writes

    storeService.find(id).map {
      case Right(FindSuccess(item)) => Ok(Json.toJson(item))
      case Left(MissingItemError) => NotFound(Json.toJson(ErrorDTO("Requested item was not found")))
      case Left(UnexpectedMessageError) => InternalServerError(Json.toJson(ErrorDTO("An unexpected error occurred")))
    }
  }
}
