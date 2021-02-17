package streams

import org.scalatest.funsuite.AnyFunSuite

class DataSourcesTest extends AnyFunSuite {
  val ds = new DataSources()
  val cust = ds.customers
  val menus = ds.menuItems
  val stf = ds.staff
  val ing = ds.ingredients

  test("The first lines of the data have collected") {
    assert(cust.headOption == Some(Customer(0, "MARGARET", "MARGARET@gmail.com")))
    assert(menus.headOption == Some(MenuItem(1, "Consomme printaniere royal", "", 1, 0.4)))
    assert(stf.headOption == Some(Staff(0, "MARGARET")))
    assert((ing.headOption) == Some(Ingredient(1, "Angelica", "Angelica keiskei", "Angelica is a genus of about 60" +
      " species of tall biennial and perennial herbs in the family Apiaceae, native to temperate and subarctic regions" +
      " of the Northern Hemisphere, reaching as far north as Iceland and Lapland. They grow to 1–3 m tall, with large" +
      " bipinnate leaves and large compound umbels of white or greenish-white flowers. Some species can be found in" +
      " purple moor and rush pastures.")))
  }



}
