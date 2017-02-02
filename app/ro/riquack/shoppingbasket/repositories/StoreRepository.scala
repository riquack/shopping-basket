package ro.riquack.shoppingbasket.repositories

import java.util.UUID

import ro.riquack.shoppingbasket.models.{Item, Review, StoreItem}

import scala.util.Random

class StoreRepository {

  def all: List[StoreItem] = (1 to 100).map { _ => StoreItem(addItem,  Random.nextInt(10)) }.toList

  private def addItem = Item(
    UUID.randomUUID().toString,
    Random.alphanumeric.take(12).mkString,
    Random.alphanumeric.take(8).mkString,
    Random.alphanumeric.take(10).mkString,
    Random.alphanumeric.take(70).mkString,
    BigDecimal(Random.nextInt(1000)),
    addReviews(Random.nextInt(4))
  )

  private def addReviews(numberOfReviews: Int) =
    (1 to numberOfReviews).map { _ =>
      Review(
        Random.nextInt(5),
        Some(Random.alphanumeric.take(20).mkString)
      )
    }.toList

}
