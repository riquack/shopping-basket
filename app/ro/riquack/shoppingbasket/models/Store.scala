package ro.riquack.shoppingbasket.models

import play.api.libs.json.{Json, Writes}
import ro.riquack.shoppingbasket.api.dto.ItemDTO

case class Store(private var items: List[Item]) {

  def removeStock(itemDTO: ItemDTO): Unit = {
    items = items.map(item => if (item.id == itemDTO.id) item.copy(stock = item.stock - itemDTO.amount) else item)
  }

  def addStock(basketItem: BasketItem): Unit =
    items = items.map(item => if (item.id == basketItem.item.id) item.copy(stock = item.stock + basketItem.amount) else item)

  def find(id: String): Option[Item] = items.find(_.id == id)

}
object Store {
  import Item.lightWrites
  implicit val writes: Writes[Store] = Json.writes[Store]
}
