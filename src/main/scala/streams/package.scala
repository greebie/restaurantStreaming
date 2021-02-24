import com.typesafe.config.{Config, ConfigFactory}
import configs.syntax._
import java.util.UUID
import akka.event.slf4j.Logger

package object streams {
  final val root = "akka"
  final val env = if (System.getenv("DEPLOYMENT") == null) "development" else System.getenv("DEPLOYMENT")
  val prefix: String = root + '.' + env
  val logger = Logger("Streams package")
  val empty_keys = ApiKeys(List.empty[ApiKey])
  val conf: Config = ConfigFactory.load()
  val api_keyslist: List[UUID] = conf
    .get[List[String]](prefix + ".streams.keys").value.map(n => UUID.fromString(n))
  val api_nameslist: List[String] = conf.get[List[String]](prefix + ".streams.names").value
  val api_keys: ApiKeys = ApiKeys(api_keyslist.zip(api_nameslist).map(n => ApiKey(n._1, n._2)))
  val api_blocked: ApiKeys = conf.get[ApiKeys]("api_keys_blocked").valueOrElse(empty_keys)
  val api_blockedlist: List[UUID] = api_blocked.keys.map(_.id)
  val bucket: String = "restaurantstream"
  val ip: String = conf.get[String](prefix + ".ip").valueOrElse("localhost")
  val port: Int = conf.get[Int](prefix + ".port").valueOrElse(8080)
}
