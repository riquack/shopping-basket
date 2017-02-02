package ro.riquack.shoppingbasket

import ro.riquack.shoppingbasket.models._

trait TestValues {

  val phoneReview = List(Review(5, Some("Best phone ever!")), Review(4, None), Review(4, None))

  val phone = Item("ae4cd", "iPhone 7", "Apple", "phones", "Best iPhone ever build", BigDecimal(700), phoneReview)
  val notebook = Item("zf33s", "MacBook Pro 15'", "Apple", "notebooks", "Take your workstation wherever you go", BigDecimal(2100), List.empty)
  val bike = Item("bla32", "Ranger", "Kilimanjaro", "bike", "Conquer the world on two wheels", BigDecimal(643), List.empty)

  val stockPhone = StoreItem(phone, 10)
  val stockNotebook = StoreItem(notebook, 5)
  val stockBike = StoreItem(bike, 4)

  val basketPhone = BasketItem(phone, 2)
  val basketNotebook = BasketItem(notebook, 1)
  val basketBike = BasketItem(bike, 1)

  def defaultStore = Store(List(stockPhone, stockNotebook, stockBike))

  def defaultBasket = Basket(List(basketPhone))

}
