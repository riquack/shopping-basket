package ro.riquack.shoppingbasket.services.responses

import ro.riquack.shoppingbasket.models.StoreItem

trait StoreServiceResponse

object StoreServiceResponse {
  case class RetrieveSuccess(storeItems: List[StoreItem]) extends StoreServiceResponse
  case class FindSuccess(store: StoreItem) extends StoreServiceResponse
}
