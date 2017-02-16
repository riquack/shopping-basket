package ro.riquack.shoppingbasket.repositories

import java.util.UUID

import ro.riquack.shoppingbasket.models.{Basket, BasketItem, Item}

import scala.collection.mutable


class BasketRepository {

  private val storage = mutable.HashMap.empty[String, Basket]

  def create(): String = {
    val uuid = UUID.randomUUID().toString
    storage.put(uuid, new Basket(List.empty[BasketItem]))
    uuid
  }

  def retrieve(id: String): Option[Basket] = storage.get(id)

  def addTo(basketId: String, item: Item, amount: Int): Unit =
    storage(basketId).add(item, amount)

  def removeFrom(basketId: String, basketItem: BasketItem): Unit =
    storage(basketId).remove(basketItem)

}
