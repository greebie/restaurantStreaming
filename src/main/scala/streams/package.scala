import com.typesafe.config.ConfigFactory
import configs.{ConfigReader, Result}
import configs.syntax._
import java.util.UUID

import akka.event.slf4j.Logger
import akka.http.scaladsl.model.DateTime


package object streams {
  val env = if (System.getenv("SCALA_ENV") == null) "development" else System.getenv("SCALA_ENV")
  val logger = Logger("Streams package")
  val empty_keys = ApiKeys(List.empty[ApiKey])
  val conf = ConfigFactory.load()
  val api_keyslist: List[UUID] = conf
    .get[List[String]](env + ".streams.keys").value.map(n => UUID.fromString(n))
  val api_nameslist = conf.get[List[String]](env + ".streams.names").value
  val api_keys: ApiKeys = ApiKeys(api_keyslist.zip(api_nameslist).map(n => ApiKey(n._1, n._2)))
  val api_blocked: ApiKeys = conf.get[ApiKeys]("api_keys_blocked").valueOrElse(empty_keys)
  val api_blockedlist: List[UUID] = api_blocked.keys.map(_.id)
  val bucket: String = "restaurantstream"
}
