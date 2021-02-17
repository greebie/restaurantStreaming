package streams

import org.scalatest.funsuite.AnyFunSuite
import akka.stream.scaladsl.{Sink, Source}
import org.scalatest.concurrent.{ScalaFutures}
import scala.concurrent.Future
import akka.util.ByteString
import akka.stream.alpakka.s3.scaladsl.{S3}
import akka.stream.alpakka.s3._

class StreamsTest extends AnyFunSuite with ScalaFutures {
  implicit val ec = scala.concurrent.ExecutionContext.global



}
