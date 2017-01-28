package ro.riquack.shoppingbasket.models

import play.api.libs.json._

case class Review(rating: Int, description: Option[String]) {

}

object Review {
  implicit val  writes: Writes[Review] = Json.writes[Review]
}