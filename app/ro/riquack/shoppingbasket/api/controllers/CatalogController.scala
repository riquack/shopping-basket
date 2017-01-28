package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.models.{CatalogGenerator, Item}

import scala.concurrent.Future

@Singleton
class CatalogController @Inject extends Controller{

  val catalog = CatalogGenerator.generate

  def list = Action.async {

    import Item.lightWrites
    Future.successful(Ok(Json.toJson(catalog)))

  }

  def find(id: String) = Action.async {
    import Item.writes
    Future.successful(
      catalog.find(_.id == id) match {
        case Some(item) => Ok(Json.toJson(item))
        case None => NotFound
      }
    )
  }
}
