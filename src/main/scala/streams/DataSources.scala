package streams

import java.io.{InputStream, InputStreamReader}
import java.nio.charset.StandardCharsets
import java.util.UUID
import akka.http.scaladsl.model.DateTime
import com.github.tototoshi.csv.CSVReader._

trait Item

final case class OrderList(list: List[Order])
final case class ErrorMessage(key:String, value: String)
final case class Staff (id: Long, name: String)
final case class Customer (id: Long, name: String, email: String)
final case class Order(id: UUID, staff: Staff, customer: Customer, menuItems: List[MenuItem])
final case class MenuItem(id: Long,
                    name: String,
                    description: String,
                    recipe: Long,
                    price: Double) extends Item
final case class Ingredient(id: Long, name: String, name_scientific: String, description: String) extends Item
final case class Delivery(id: UUID, ordered: DateTime, arrived: Option[DateTime], signed: Staff, items: List[Item])
final case class ApiKey(id: UUID, name: String)

final case class ApiKeys(keys: List[ApiKey]) {
  def getUser(id: UUID): ApiKey = {
    keys.filter(_.id == id)(0)
  }
}

class DataSources {
  val in_food: InputStream = getClass.getResourceAsStream("/Food.csv")
  val in_menu: InputStream = getClass.getResourceAsStream("/Dish.csv")
  val reader_food: InputStreamReader = new InputStreamReader(in_food, StandardCharsets.UTF_8)
  val reader_menu: InputStreamReader = new InputStreamReader(in_menu, StandardCharsets.UTF_8)
  val ingredients : IndexedSeq[Ingredient] = open(reader_food)
    .all()
    .map(m => {
      Ingredient(m(0).toLong, m(1), m(2), m(3))})
    .toIndexedSeq

  val menuItems: IndexedSeq[MenuItem] = open(reader_menu)
    .all()
    .filterNot(f => f(8) == "")
    .map{ m   =>
      val id = Option(m(0).toLong).getOrElse(-1L)
      val price = Option(m(8).toDouble).getOrElse(0.0)
      MenuItem(id, m(1), m(2), id, price)
    }
    .filterNot(_.id == -1)
    .toIndexedSeq

  val staff : IndexedSeq[Staff] = scala.io.Source.fromResource("names.txt")("UTF-8")
    .getLines()
    .map(_.trim)
    .zipWithIndex
    .map({ case(name: String, index: Int) =>
      Staff(index.toLong, name)
    }).toIndexedSeq

  val customers : IndexedSeq[Customer] = scala.io.Source.fromResource("names.txt")("UTF-8")
    .getLines()
    .map(_.trim)
    .zipWithIndex
    .map({case (name: String, index: Int) =>
      val namet: String = name.trim
      Customer(index.toLong, namet, namet + "@gmail.com")
    })
    .toIndexedSeq
}
