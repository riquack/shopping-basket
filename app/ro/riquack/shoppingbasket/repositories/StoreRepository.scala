package ro.riquack.shoppingbasket.repositories

import java.util.UUID

import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.models.{BasketItem, Item, Review, StoreItem}

import scala.collection.mutable
import scala.util.Random



/**
  * The content of the store is generated and held in memory.
  * In a real world scenario this information is persisted in an external storage (such as a database or service) and
  * methods in this class provide an abstraction for the interaction with that storage.
  */
class StoreRepository {

  private val index: mutable.HashMap[String, StoreItem] =
    mutable.HashMap((1 to 100).map { _ =>
      val storeItem = StoreItem(addItem,  Random.nextInt(10))
      storeItem.item.id -> storeItem
    }: _*)

  def list(): List[StoreItem] = index.values.toList

  def retrieve(id: String): Option[StoreItem] = index.get(id)

  def updateStock(id: String, amount: Int): Unit = {
    val value = index(id)
    index.update(id, value.copy(stock = value.stock + amount))
  }

  def decreaseStock(itemDTO: ItemDTO) = updateStock(itemDTO.id, -itemDTO.amount): Unit

  def incrementStock(basketItem: BasketItem) = updateStock(basketItem.item.id, basketItem.amount): Unit


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
