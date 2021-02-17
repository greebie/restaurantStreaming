package streams

import java.util.UUID

import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat}

object JsonProtocols extends DefaultJsonProtocol{
  implicit object UUIDFormat extends JsonFormat[UUID] {
    def write(uuid: UUID) = JsString(uuid.toString)
    def read(value: JsValue):UUID = {
      value match {
        case JsString(uuid) => UUID.fromString(uuid)
        case _              => throw DeserializationException("Expected hexadecimal UUID string")
      }
    }
  }
  implicit val menuItemFormat: RootJsonFormat[MenuItem] = jsonFormat5(MenuItem)
  implicit val errorMesFormat: RootJsonFormat[ErrorMessage] = jsonFormat2(ErrorMessage)
  implicit val staffEqFormat: RootJsonFormat[Staff] = jsonFormat2(Staff)
  implicit val customerEqFormat: RootJsonFormat[Customer] = jsonFormat3(Customer)
  implicit val orderEqFormat: RootJsonFormat[Order] = jsonFormat4(Order)
  implicit val orderListFormat: RootJsonFormat[OrderList] = jsonFormat1(OrderList)
  implicit val authFormat: RootJsonFormat[Auth] = jsonFormat2(Auth)
}
