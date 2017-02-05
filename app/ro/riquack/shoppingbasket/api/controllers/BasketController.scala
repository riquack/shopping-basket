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

  def list = Action.async { implicit request =>
    basketService.list.map {
      case Right(RetrieveSuccess(basket)) => Ok(Json.toJson(basket))
      case Left(UnexpectedMessageError) => ControllerMessages.internalServerError
    }

  }

  def add = Action.async(parse.json) { implicit request =>
    request.body.validate[ItemDTO].fold(
      errors =>
        Future.successful(BadRequest(JsError.toJson(errors))),
      item =>
        basketService.add(item).map {
          case Right(Success) => Created.withHeaders(LOCATION -> routes.BasketController.list().url)
          case Left(MissingItemError) => ControllerMessages.notFound
          case Left(InsufficientStockError) => ControllerMessages.insufficientStock
          case Left(UnexpectedMessageError) => ControllerMessages.internalServerError
        }
    )
  }

  def remove(id: String) = Action.async { implicit request =>
    basketService.remove(id).map {
      case Right(Success) => Ok
      case Left(MissingItemError) => ControllerMessages.notFound
      case Left(UnexpectedMessageError) => ControllerMessages.internalServerError

    }
  }

}