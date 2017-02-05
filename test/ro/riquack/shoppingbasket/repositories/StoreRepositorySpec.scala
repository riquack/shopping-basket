package ro.riquack.shoppingbasket.repositories

import org.scalatest.{FlatSpec, MustMatchers, OptionValues}
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.models.BasketItem

/**
  * Created by p3700409 on 05/02/2017.
  */
class StoreRepositorySpec extends FlatSpec with OptionValues with TestValues with MustMatchers {

  val store = new StoreRepository

  "A StoreRepository" should "list all existing items" in {
    store.list().size mustEqual 100
  }

  it should "find a existing item" in {
    val expectedItem = store.list().head
    store.retrieve(expectedItem.item.id).value mustEqual expectedItem
  }

  it should "not find a missing item" in {
    store.retrieve("b54cz") mustEqual None
  }

  it should "add stock for item removed from basket" in {
    val expectedItem = store.list().head
    store.incrementStock(BasketItem(expectedItem.item, 1))
    store.retrieve(expectedItem.item.id).value.stock == expectedItem.stock + 1
  }

  it should "remove from stock the specified amount" in {
    val expectedItem = store.list().head
    store.incrementStock(BasketItem(expectedItem.item, 1))
    store.retrieve(expectedItem.item.id).value.stock == expectedItem.stock - 1
  }
}
