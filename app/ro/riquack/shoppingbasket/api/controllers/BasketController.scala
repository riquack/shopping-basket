package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.services.BasketService

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class BasketController @Inject() (basketService: BasketService)(implicit ec: ExecutionContext) extends Controller {

  def list = Action.async { implicit request =>

    import ro.riquack.shoppingbasket.models.BasketItem.writes
    basketService.list.map(basket => Ok(Json.toJson(basket)))

  }

  def add = Action.async(parse.json) { implicit request =>

    request.body.validate[ItemDTO].fold(
      errors =>
        Future.successful(BadRequest(JsError.toJson(errors))),
      item =>
        basketService.add(item).map {
          case Left(_) => ???
          case Right(_) => Created.withHeaders(LOCATION -> routes.BasketController.list().url)

        }
    )
  }

  def remove(id: String) = Action.async {
    Future.successful(Ok("You have removed an item from the basket."))
  }

}