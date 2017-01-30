package ro.riquack.shoppingbasket.models

import org.scalatest.FlatSpec
import ro.riquack.shoppingbasket.TestValues

class ItemSpec extends FlatSpec with TestValues {

  "A item" should "compute the average rating" in {
    assert(phone.rating == 4.33)
    assert(bike.rating == 0.00)
  }

}
