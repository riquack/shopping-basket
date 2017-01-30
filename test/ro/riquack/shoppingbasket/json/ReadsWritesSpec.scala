package ro.riquack.shoppingbasket.json

import org.scalatest.{FlatSpec, MustMatchers}
import play.api.libs.json.Json
import ro.riquack.shoppingbasket.TestValues

class ReadsWritesSpec extends FlatSpec with MustMatchers with TestValues {

  "A Basket" should "have the following JSON representation" in {
    import ro.riquack.shoppingbasket.models.Basket.writes

    val basket = defaultBasket
    val basketJson = Json.toJson(basket)
    val itemJson = (basketJson \ "items")(0)

    (itemJson \ "item" \ "id").as[String] mustEqual "ae4cd"
    (itemJson \ "item" \ "name").as[String] mustEqual "iPhone 7 by Apple"
    (itemJson \ "item" \ "price").as[BigDecimal] mustEqual BigDecimal(700)
    (itemJson \ "amount" ).as[Int] mustEqual 1
    (basketJson \ "value").as[BigDecimal] mustEqual BigDecimal(700)
  }

  "A Store" should "have the following JSON representation" in {
    import ro.riquack.shoppingbasket.models.Store.writes
    val store = defaultStore

    val storeJson = Json.toJson(store)
    val itemJson = (storeJson \ "items")(0)

    (itemJson \ "id").as[String] mustEqual "ae4cd"
    (itemJson \ "name").as[String] mustEqual "iPhone 7 by Apple"
    (itemJson \ "description").as[String] mustEqual "Best iPhon..."
    (itemJson \ "price").as[BigDecimal] mustEqual BigDecimal(700)
    (itemJson \ "rating").as[Double] mustEqual 4.33
    (itemJson \ "inStock").as[Boolean] mustEqual true
  }

  "An Item" should "have the following JSON representation" in {
    import ro.riquack.shoppingbasket.models.Item.writes

    val itemJson = Json.toJson(phone)
    val reviewJson = (itemJson \ "reviews")(0)

    (itemJson \ "id").as[String] mustEqual "ae4cd"
    (itemJson \ "name").as[String] mustEqual "iPhone 7"
    (itemJson \ "vendor").as[String] mustEqual "Apple"
    (itemJson \ "category").as[String] mustEqual "phones"
    (itemJson \ "description").as[String] mustEqual "Best iPhone ever build"
    (itemJson \ "price").as[BigDecimal] mustEqual BigDecimal(700)
    (itemJson \ "stock").as[Int] mustEqual 10

    (reviewJson \ "rating").as[Int] mustEqual 5
    (reviewJson \ "description").as[String] mustEqual "Best phone ever!"
  }

  "An ItemDTO" should "be created from the following" in {
    import ro.riquack.shoppingbasket.api.dto.ItemDTO.format
    val input = """{"id":"sa31-es1-43c","amount":1}"""
    val item = Json.parse(input)

    (item \ "id").as[String] mustEqual "sa31-es1-43c"
    (item \ "amount").as[Int] mustEqual 1
  }

}
