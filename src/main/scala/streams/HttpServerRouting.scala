package streams

import java.util.UUID

import akka.NotUsed
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.Behaviors
import akka.event.slf4j.Logger
import akka.http.javadsl.common.JsonEntityStreamingSupport
import akka.http.scaladsl.Http
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ThrottleMode
import akka.stream.scaladsl.Source
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object HttpServerRouting {
  val logger = Logger("StreamLog")
  import JsonProtocols._
  val generate = new GenerateOrders()
  object Api {
    def fetchStream(num: Int): Option[OrderList] = {
      Option(OrderList(generate.getNextOrders(num: Int).map(n => {
        Order(n.id, n.staff, n.customer, n.menuItems)
      }).toList))
    }
    def apply: Behaviors.Receive[RequestGetter] = apply(List.empty)
    def apply(orders: List[Order]): Behaviors.Receive[RequestGetter] = Behaviors.receive {
      case (_, GetOrders(replyTo)) =>
        val orderL = fetchStream(100) match {
          case Some(items) => items
          case None => OrderList(List[Order]())
        }
        replyTo ! orderL
        Behaviors.same
    }
  }

  def authenticate(key: UUID): Auth = {
    val keys: List[UUID] = api_keyslist
    val blocked: List[UUID] = api_blockedlist
    val result = if (keys.contains(key))
      { Auth("VALID", "Api key verified")
      } else if (blocked.contains(key)) {
        Auth("BLOCKED", "Key has been blocked. " +
          "We have added this IP to the blocked list. " +
          "If you feel this is in error, please contact your system administrator")
      } else {
      Auth("INVALID", "Api key not found")
    }
    result
  }

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[RequestGetter] = ActorSystem(Api.apply, "restaurantStream")
    implicit val executionContext: ExecutionContext = system.executionContext
    val stream: ActorRef[RequestGetter] = system
    implicit val jsonStreamingSupport : JsonEntityStreamingSupport = EntityStreamingSupport.json()
    lazy val route: Route =
      path("orders") {
        get {
          parameters("api_key") { key =>
            implicit val timeout: Timeout = 5.seconds
            val auth = authenticate(UUID.fromString(key))
            val orders: Source[Order, NotUsed] = Source.fromIterator(() => generate.ordersIt)
              .limit(10000)
              .takeWithin(new FiniteDuration(2, SECONDS))
              .throttle(generate.rnd.nextInt(1000),
                1.second)
            if (auth.resp == "VALID") {
              val user = api_keys.getUser(UUID.fromString(key))
              logger.info(f"User $user%s accessed the api successfully")
              complete(orders)
            } else {
              logger.warn(f"Unknown user tried key $key%s but failed.")
              logger.info(f"${api_keyslist.toString}%s")
              complete(auth)
            }
          }
        }
      }

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}

}
