package ro.riquack.shoppingbasket.services.responses

trait StoreServiceError

object StoreServiceError {

  case object UnexpectedMessageError extends StoreServiceError
  case object MissingItemError extends StoreServiceError

}
