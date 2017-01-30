package ro.riquack.shoppingbasket

import ro.riquack.shoppingbasket.models.Item

trait TestValues {

  val phone = Item("ae4cd", "iPhone 7", "Apple", "phones", "Best iPhone ever build", BigDecimal(700), List.empty, 10)
  val notebook = Item("zf33s", "MacBook Pro 15'", "Apple", "notebooks", "Take your workstation wherever you go", BigDecimal(2100), List.empty, 5)
  val bike = Item("bla32", "Ranger", "Kilimanjaro", "bike", "Conquer the world on two wheels", BigDecimal(643), List.empty, 4)

}
