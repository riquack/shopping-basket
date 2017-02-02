package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.api.dto.{ErrorDTO, ItemDTO}
import ro.riquack.shoppingbasket.services.BasketService
import ro.riquack.shoppingbasket.services.responses.BasketServiceResponse.{RetrieveSuccess, Success}
import ro.riquack.shoppingbasket.services.responses.BasketServiceError.{InsufficientStockError, MissingItemError, UnexpectedMessageError}

import scala.concurrent.{ExecutionContext, Future}


class BasketController @Inject() (basketService: BasketService)(implicit ec: ExecutionContext) extends Controller {

  def list = Action.async { implicit request =>
    import ro.riquack.shoppingbasket.models.BasketItem.writes
    import ErrorDTO.writes

    basketService.list.map {
      case Right(RetrieveSuccess(basket)) => Ok(Json.toJson(basket))
      case Left(UnexpectedMessageError) => InternalServerError(Json.toJson(ErrorDTO("An unexpected error occurred")))
    }

  }

  def add = Action.async(parse.json) { implicit request =>
    request.body.validate[ItemDTO].fold(
      errors =>
        Future.successful(BadRequest(JsError.toJson(errors))),
      item =>
        basketService.add(item).map {
          case Right(Success) => Created.withHeaders(LOCATION -> routes.BasketController.list().url)
          case Left(MissingItemError) => NotFound(Json.toJson(ErrorDTO("Requested item was not found")))
          case Left(InsufficientStockError) => BadRequest(Json.toJson(ErrorDTO("Not enough stock for the request item")))
          case Left(UnexpectedMessageError) => InternalServerError(Json.toJson(ErrorDTO("An unexpected error occurred")))
        }
    )
  }

  def remove(id: String) = Action.async { implicit request =>
    basketService.remove(id).map {
      case Right(Success) => Ok
      case Left(MissingItemError) => NotFound(Json.toJson(ErrorDTO("Requested item was not found")))
      case Left(UnexpectedMessageError) => InternalServerError(Json.toJson(ErrorDTO("An unexpected error occurred")))

    }
  }

}