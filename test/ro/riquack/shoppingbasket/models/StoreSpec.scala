package ro.riquack.shoppingbasket.models

import org.scalatest._
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.api.dto.ItemDTO

class StoreSpec extends FlatSpec with TestValues with OptionValues {

  private val store = defaultStore

  "A store" should "find a existing item" in {
    assert(store.retrieve("ae4cd").value == stockPhone)
  }

  it should "not find a missing item" in {
    assert(store.retrieve("b54cz").isEmpty)
  }

  it should "remove from stock the specified amount" in {
    store.removeStock(ItemDTO("ae4cd", 1))
    assert(store.retrieve("ae4cd").value.stock == 9)
  }

  it should "add stock for item removed from basket" in {
    store.addStock(BasketItem(bike, 1))
    assert(store.retrieve("bla32").value.stock == 5)
  }



}
