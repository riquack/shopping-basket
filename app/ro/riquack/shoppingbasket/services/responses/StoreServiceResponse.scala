package ro.riquack.shoppingbasket.services.responses

import ro.riquack.shoppingbasket.models.{Store, StoreItem}

trait StoreServiceResponse

object StoreServiceResponse {
  case class RetrieveSuccess(store: Store) extends StoreServiceResponse
  case class FindSuccess(store: StoreItem) extends StoreServiceResponse
}
