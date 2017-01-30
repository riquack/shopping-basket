package ro.riquack.shoppingbasket.models

import org.scalatest.FlatSpec
import ro.riquack.shoppingbasket.TestValues

class ItemSpec extends FlatSpec with TestValues {

  val reviews = List(Review(5, Some("Best phone ever!")), Review(4, None), Review(4, None))

  "A item" should "compute the average rating" in {
    assert(phone.copy(reviews = reviews).rating == 4.33)
    assert(phone.rating == 0.00)
  }

}
