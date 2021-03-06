package ro.riquack.shoppingbasket.api.controllers

import play.api.libs.json.Json
import ro.riquack.shoppingbasket.api.dto.ErrorDTO
import play.api.mvc.Results._

object ControllerMessages {

  val insufficientStock = BadRequest(Json.toJson(ErrorDTO("Not enough stock for the request item.")))
  val itemNotFound = NotFound(Json.toJson(ErrorDTO("Requested item was not found.")))
  val basketNotFound = NotFound(Json.toJson(ErrorDTO("Requested basket was not found.")))
  val internalServerError = InternalServerError(Json.toJson(ErrorDTO("An unexpected error occurred.")))
  val wrongPayload = BadRequest(Json.toJson(ErrorDTO("Supplied payload does not have the required elements.")))

}
