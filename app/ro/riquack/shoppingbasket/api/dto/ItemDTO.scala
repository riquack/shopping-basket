package ro.riquack.shoppingbasket.api.dto

import play.api.libs.json._

case class ItemDTO(id: String, amount: Int) {

}

object  ItemDTO {
  implicit val format: Format[ItemDTO] = Json.format[ItemDTO]
}
