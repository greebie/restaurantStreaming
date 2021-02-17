package streams

import akka.actor.typed.ActorRef

trait RequestGetter

final case class Auth(resp: String, message: String)
final case class GetOrders(replyTo: ActorRef[OrderList]) extends RequestGetter


