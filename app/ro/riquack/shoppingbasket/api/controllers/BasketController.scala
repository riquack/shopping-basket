package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.models.CatalogGenerator

import scala.concurrent.Future


@Singleton
class BasketController @Inject extends Controller {

  def list = Action.async {
    Future.successful(Ok("Your list of items in the basket."))
  }

  def add = Action.async {
    Future.successful(Ok("You have added an item to the basket."))
  }

  def remove(id: String) = Action.async {
    Future.successful(Ok("You have removed an item from the basket."))
  }

}