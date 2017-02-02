package ro.riquack.shoppingbasket.services.responses

trait BasketServiceError

object BasketServiceError {
  case object UnexpectedMessageError extends BasketServiceError
  case object InsufficientStockError extends BasketServiceError
  case object MissingItemError extends BasketServiceError
}


