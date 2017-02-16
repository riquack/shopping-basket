package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.services.BasketService
import ro.riquack.shoppingbasket.services.responses.BasketServiceResponse._
import ro.riquack.shoppingbasket.services.responses.BasketServiceError._

import scala.concurrent.{ExecutionContext, Future}


class BasketController @Inject() (basketService: BasketService)(implicit ec: ExecutionContext) extends Controller {

  def create = Action.async { implicit request =>
    basketService.create.map {
      case Right(CreateSuccess(basketId)) => Created.withHeaders(LOCATION -> routes.BasketController.list(basketId).url)
      case Left(UnexpectedMessageError) => ControllerMessages.internalServerError
    }
  }

  def list(basketId: String) = Action.async { implicit request =>
    basketService.list(basketId).map {
      case Right(RetrieveSuccess(basket)) => Ok(Json.toJson(basket))
      case Left(UnexpectedMessageError) => ControllerMessages.internalServerError
      case Left(MissingBasketError) => ControllerMessages.basketNotFound
    }

  }

  def add(basketId: String) = Action.async(parse.json) { implicit request =>
    request.body.validate[ItemDTO].fold(
      errors =>
        Future.successful(BadRequest(JsError.toJson(errors))),
      item =>
        basketService.add(basketId, item).map {
          case Right(Success) => Created.withHeaders(LOCATION -> routes.BasketController.list(basketId).url)
          case Left(MissingItemError) => ControllerMessages.itemNotFound
          case Left(MissingBasketError) => ControllerMessages.basketNotFound
          case Left(InsufficientStockError) => ControllerMessages.insufficientStock
          case Left(UnexpectedMessageError) => ControllerMessages.internalServerError
        }
    )
  }

  def remove(basketId: String, itemId: String) = Action.async { implicit request =>
    basketService.remove(basketId, itemId).map {
      case Right(Success) => Ok
      case Left(MissingItemError) => ControllerMessages.itemNotFound
      case Left(MissingBasketError) => ControllerMessages.basketNotFound
      case Left(UnexpectedMessageError) => ControllerMessages.internalServerError

    }
  }

}