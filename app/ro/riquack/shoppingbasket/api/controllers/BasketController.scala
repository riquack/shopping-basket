package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.services.BasketService

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class BasketController @Inject() (basketService: BasketService)(implicit ec: ExecutionContext) extends Controller {

  def list = Action.async {

    import ro.riquack.shoppingbasket.models.Item.lightWrites
    basketService.list.map(items => Ok(Json.toJson(items)))

  }

  def add = Action.async {
    Future.successful(Ok("You have added an item to the basket."))
  }

  def remove(id: String) = Action.async {
    Future.successful(Ok("You have removed an item from the basket."))
  }

}