package streams

import org.scalatest.funsuite.AnyFunSuite

class GenerateOrdersTest extends AnyFunSuite {
  val order = new GenerateOrders()
  val order_seeded = new GenerateOrders(seed = Some(12345))

  test("Orders are created properly") {
    ///order.getOrders.take(5).foreach(println)
    assert (order.getMenuItem.isInstanceOf[MenuItem])
    assert (order.getCustomer.isInstanceOf[Customer])
    assert (order.getOrder.isInstanceOf[Order])
    assert (order.orders.head.isInstanceOf[Order])
    assert (order.getStaff.isInstanceOf[Staff])
  }

  test("Random result as expected") {
    // order of vals matters because we are testing Random(12345)
    val menuTest = order_seeded.getMenuItem
    val customerTest = order_seeded.getCustomer
    val orderTest = order_seeded.getOrder
    // Add new vals here to test other items

    assert (menuTest == MenuItem(160357, "Glas Tomatensaft", "", 160357, 0.0))
    assert (customerTest == Customer(109950, "KEITH", "KEITH@gmail.com"))
    assert (orderTest == Order(orderTest.id, // UUID is unique for every compile, so cannot be tested.
      Staff(120170, "ARNOLD"),
      Customer(100461, "HAL", "HAL@gmail.com"),
      List(MenuItem(453367,"Eperlans, beau rivage","",453367,0.0),
        MenuItem(126143,"Scrambled (2 eggs)","",126143,0.25),
        MenuItem(167777,"Cabernet Sauvignon, Cain Vineyards Cuvee, 1997. (Napa Valley)","",167777,44.0) ,
        MenuItem(92027,"Emporer steak","",92027,0.0),
        MenuItem(61324,"Soup a la Clermont.","",61324,0.0),
        MenuItem(501718,"Vanilla Corn Starch with Whipped or Ice Cream","",501718,0.1),
        MenuItem(447456,"Med. sirloin steak, onions","",447456,1.35))))
  }
}
