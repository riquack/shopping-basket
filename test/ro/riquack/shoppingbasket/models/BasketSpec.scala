package ro.riquack.shoppingbasket.models

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import ro.riquack.shoppingbasket.TestValues


@RunWith(classOf[JUnitRunner])
class BasketSpec extends FlatSpec with TestValues with OptionValues {

  val phoneBasket = BasketItem(phone, 2)
  val notebookBasket = BasketItem(notebook, 1)
  val basketItem = BasketItem(bike, 1)

  val basket = Basket(List(phoneBasket, notebookBasket))

  "A basket" should "calculate the value of the items it contains" in {
    assert(basket.value == BigDecimal(3500))
  }

  it should "find an exiting item" in {
    assert(basket.find("ae4cd").value == phoneBasket)
  }

  it should "not find a missing item" in {
    assert(basket.find("3d4ea").isEmpty)
  }

  it should "contain a newly added item" in {
    basket.add(bike, 1)
    assert(basket.find("bla32").value == basketItem)
  }

  it should "update the amount for an existing item" in {
    basket.add(phone, 1)
    assert(basket.find("ae4cd").value == BasketItem(phone, 3))
  }

  it should "not contain a previously removed item" in {
    basket.remove(notebookBasket)
    assert(basket.find("ae4cd").isDefined)
    assert(basket.find("zf33s").isEmpty)
  }

}
