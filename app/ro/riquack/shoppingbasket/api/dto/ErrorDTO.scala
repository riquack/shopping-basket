package ro.riquack.shoppingbasket.api.dto

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Writes._

case class ErrorDTO(description: String) {
  lazy val date: String = java.time.LocalDateTime.now().toString
  lazy val id: String = UUID.randomUUID().toString
}

object ErrorDTO {
  implicit val writes: Writes[ErrorDTO] =
    (
      (__ \ 'id).write[String] and
        (__ \ 'date).write[String] and
        (__ \ 'description).write[String]
      )(error => (
      error.id,
      error.date,
      error.description
    ))
}
