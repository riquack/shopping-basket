package ro.riquack.shoppingbasket.controllers

import akka.stream.Materializer
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import org.mockito.Mockito._
import org.mockito.Matchers._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import ro.riquack.shoppingbasket.TestValues
import ro.riquack.shoppingbasket.api.controllers.BasketController
import ro.riquack.shoppingbasket.api.dto.ItemDTO
import ro.riquack.shoppingbasket.services._
import ro.riquack.shoppingbasket.services.responses.BasketServiceError.{InsufficientStockError, MissingItemError}
import ro.riquack.shoppingbasket.services.responses.BasketServiceResponse.{RetrieveSuccess, Success}

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class BasketControllerSpec extends PlaySpec with MockitoSugar with OneAppPerSuite with TestValues {

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit lazy val materializer: Materializer = app.materializer

  private val mockBasketService = mock[BasketService]
  private val controller = new BasketController(mockBasketService)
  private val basketId = "1s-ve"

  "A BasketController" should {

    "retrieve the list of items in the basket" in {
      when(mockBasketService.list(any[String])) thenReturn Future(Right(RetrieveSuccess(defaultBasket)))

      val result = controller.list(basketId)(FakeRequest(GET, "/basket/items"))
      assert(status(result) == OK)
    }

    "successfully add an existing item if the stock is sufficient" in {
      when(mockBasketService.add(any[String], any[ItemDTO])) thenReturn Future(Right(Success))

      val req = FakeRequest(POST, "/basket/items")
        .withBody("""{"id":"as31fs","amount":3}""")
        .withHeaders(CONTENT_TYPE -> "application/json")

      val result = call(controller.add(basketId),req)
      assert(status(result) == CREATED)
    }


    "inform the user that there is not enough stock for an existing item" in {
      when(mockBasketService.add(any[String], any[ItemDTO])) thenReturn Future(Left(InsufficientStockError))

      val req = FakeRequest(POST, "/basket/items")
        .withBody("""{"id":"as31fs","amount":3}""")
        .withHeaders(CONTENT_TYPE -> "application/json")

      val result = call(controller.add(basketId),req)
      assert(status(result) == BAD_REQUEST)
    }

    "inform the user that the item is not valid" in {
      when(mockBasketService.add(any[String], any[ItemDTO])) thenReturn Future(Left(MissingItemError))

      val req = FakeRequest(POST, "/basket/items")
        .withBody("""{"id":"as31fs","amount":3}""")
        .withHeaders(CONTENT_TYPE -> "application/json")

      val result = call(controller.add(basketId),req)
      assert(status(result) == NOT_FOUND)
    }

    "remove an existing item from the basket" in {
      when(mockBasketService.remove(any[String], any[String])) thenReturn Future(Right(Success))

      val result = controller.remove(basketId, "asd3as")(FakeRequest("DELETE", "/basket/items/asd3as"))
      assert(status(result) == OK)
    }

    "inform the user that there is no such item in the basket" in {
      when(mockBasketService.remove(any[String], any[String])) thenReturn Future(Left(MissingItemError))

      val result = controller.remove(basketId, "asd3as")(FakeRequest("DELETE", "/basket/items/asd3as"))
      assert(status(result) == NOT_FOUND)
    }

  }

}
