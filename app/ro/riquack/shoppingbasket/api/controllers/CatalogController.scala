package ro.riquack.shoppingbasket.api.controllers

import javax.inject._

import play.api.mvc.{Action, Controller}
import ro.riquack.shoppingbasket.models.CatalogGenerator

import scala.concurrent.Future

@Singleton
class CatalogController @Inject extends Controller{

  def list = Action.async {

    Future.successful(Ok(CatalogGenerator.generate.toString()))
  }

}
