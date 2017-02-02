package ro.riquack.shoppingbasket.services.responses

import ro.riquack.shoppingbasket.models.Basket

trait BasketServiceResponse

object BasketServiceResponse {
  case object Success extends BasketServiceResponse
  case class RetrieveSuccess(basket: Basket) extends BasketServiceResponse
}



