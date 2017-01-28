package ro.riquack.shoppingbasket.models

import java.util.UUID

import scala.util.Random

object CatalogGenerator {

  def generate: List[Item] = (1 to 100).map { _ => generateItem }.toList

  private def generateItem = Item(
    UUID.randomUUID().toString,
    Random.alphanumeric.take(12).mkString,
    Random.alphanumeric.take(8).mkString,
    Random.alphanumeric.take(10).mkString,
    Random.alphanumeric.take(70).mkString,
    BigDecimal(Random.nextInt(1000)),
    generateReviews(Random.nextInt(4)),
    Random.nextInt(10)
  )

  private def generateReviews(numberOfReviews: Int) =
    (1 to numberOfReviews).map { _ =>
      Review(
        Random.nextInt(5),
        Some(Random.alphanumeric.take(20).mkString)
      )
    }.toList

}
