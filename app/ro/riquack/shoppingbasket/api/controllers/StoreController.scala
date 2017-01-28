package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.models.Item
import ro.riquack.shoppingbasket.services.StoreService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StoreController @Inject () (storeService: StoreService)(implicit ec: ExecutionContext) extends Controller {

  def list = Action.async {

    import Item.lightWrites
    storeService.all.map { items =>
      Ok(Json.toJson(items))
    }

  }

  def find(id: String) = Action.async {
    import Item.writes

    storeService.find(id).map {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }
}
