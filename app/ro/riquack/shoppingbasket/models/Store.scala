package ro.riquack.shoppingbasket.models

import play.api.libs.json.{Json, Writes}
import ro.riquack.shoppingbasket.api.dto.ItemDTO

case class Store(private var items: List[StoreItem]) {

  def removeStock(itemDTO: ItemDTO): Unit = {
    items = items.map(storeItem => if (storeItem.item.id == itemDTO.id) storeItem.copy(stock = storeItem.stock - itemDTO.amount) else storeItem)
  }

  def addStock(basketItem: BasketItem): Unit =
    items = items.map(storeItem => if (storeItem.item.id == basketItem.item.id) storeItem.copy(stock = storeItem.stock + basketItem.amount) else storeItem)

  def retrieve(id: String): Option[StoreItem] = items.find(_.item.id == id)

}
object Store {
  import StoreItem.lightWrites
  implicit val writes: Writes[Store] = Json.writes[Store]
}
