package ro.riquack.shoppingbasket.controllers

import org.junit.runner.RunWith
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import ro.riquack.shoppingbasket.api.controllers.StoreController
import ro.riquack.shoppingbasket.services.StoreService
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.junit.JUnitRunner
import play.api.test.FakeRequest
import play.api.test.Helpers._
import ro.riquack.shoppingbasket.TestValues

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class StoreControllerSpec extends PlaySpec with MockitoSugar with TestValues {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val mockStoreService = mock[StoreService]
  private val controller = new StoreController(mockStoreService)

  "A StoreController" should {

    "return a list of items in the store" in {
      when(mockStoreService.all) thenReturn Future(defaultStore)

      val result = controller.list(FakeRequest(GET, "/items"))
      assert(status(result) ==  OK)
    }
    "return a specific item if it's in the store" in {
      when(mockStoreService.find(any[String])) thenReturn Future(Some(bike))
      val result = controller.find("sda-12e")(FakeRequest(GET, "/items/sda-12e"))
      assert(status(result) ==  OK)
    }
    "return error when retrieving a missing item" in {
      when(mockStoreService.find(any[String])) thenReturn Future(None)
      val result = controller.find("sda-12e")(FakeRequest(GET, "/items/sda-12e"))
      assert(status(result) ==  NOT_FOUND)
    }
  }
}
