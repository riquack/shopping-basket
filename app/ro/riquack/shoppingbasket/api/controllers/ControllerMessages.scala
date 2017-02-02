package ro.riquack.shoppingbasket.api.controllers

import play.api.libs.json.Json
import ro.riquack.shoppingbasket.api.dto.ErrorDTO
import play.api.mvc.Results._

object ControllerMessages {

  val insufficientStock = BadRequest(Json.toJson(ErrorDTO("Not enough stock for the request item")))
  val notFound = NotFound(Json.toJson(ErrorDTO("Requested item was not found")))
  val internalServerError = InternalServerError(Json.toJson(ErrorDTO("An unexpected error occurred")))

}
