package ro.riquack.shoppingbasket

import ro.riquack.shoppingbasket.models._

trait TestValues {

  val reviews = List(Review(5, Some("Best phone ever!")), Review(4, None), Review(4, None))

  val phone = Item("ae4cd", "iPhone 7", "Apple", "phones", "Best iPhone ever build", BigDecimal(700), reviews, 10)
  val notebook = Item("zf33s", "MacBook Pro 15'", "Apple", "notebooks", "Take your workstation wherever you go", BigDecimal(2100), List.empty, 5)
  val bike = Item("bla32", "Ranger", "Kilimanjaro", "bike", "Conquer the world on two wheels", BigDecimal(643), List.empty, 4)

  def defaultStore = Store(List(phone, notebook, bike))

  def defaultBasket = Basket(List(BasketItem(phone, 1)))

}
