package streams

import scala.util.Random
import java.util.UUID
import scala.collection.immutable.LazyList

class GenerateOrders (val numOrders: Int = 100, val seed: Option[Int] = None) extends DataSources {

  def this (numOrders: Int, seed: Int) = {
    this(numOrders, Some(seed))
  }

  val maxMenuItems = 12

  var rnd = seed match {
    case Some(seed) => new Random(seed)
    case None => new Random()
  }

  def getMenuItem: MenuItem = menuItems(rnd.nextInt(menuItems.length))

  def getMenuItems: List[MenuItem] = List[MenuItem]()

  def getCustomer: Customer = {
    val i: Int = rnd.nextInt(customers.length)
    customers(i)
  }

  def getStaff: Staff = {
    val i: Int = rnd.nextInt(staff.length)
    staff(i)
  }

  def getOrder: Order = {
    val customer = getCustomer
    val staffer = getStaff
    val menuItemsList = List.fill(rnd.nextInt(maxMenuItems))(getMenuItem)
    Order(UUID.randomUUID(), staffer, customer, menuItemsList)
  }

  def ordersIt: Iterator[Order] = orders.iterator

  def orders: LazyList[Order] = LazyList.continually(getOrder)

  def getNextOrders(num: Int): Iterator[Order] = ordersIt.take(num)

  def getNextOrder: Option[Order] = ordersIt.nextOption()

}
