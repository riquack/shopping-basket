package ro.riquack.shoppingbasket.models

case class Item(
    id: String,
    name: String,
    vendor: String,
    category: String,
    description: String,
    price: BigDecimal,
    reviews: List[Review],
    stock: Int) {

  def rating: Long = reviews.foldRight(0)(_.rating + _) / reviews.size

}
