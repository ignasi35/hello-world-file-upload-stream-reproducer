package com.example.helloworld.impl

import akka.stream.scaladsl.Source
import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.example.helloworld.api._
import com.lightbend.lagom.scaladsl.api.ServiceCall
import play.api.libs.json._

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class HelloWorldServiceImpl()(
    implicit executionContext: ExecutionContext,
    materializer: Materializer
) extends HelloWorldService {

  override def hello(
      id: String
  ): ServiceCall[Source[String, NotUsed], Response1234] = ServiceCall {
    reqSource =>
      val eventualResponse123: Future[Response1234] =
        reqSource
        .runWith(Sink.seq)
        .map { seq =>
          val request123 = Json.parse(seq.mkString).as[Request1234]
          println(s"* * * * Handled request ${request123}")
          Response1234(id)
        }
      eventualResponse123
  }

}
