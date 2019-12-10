package com.example.helloworld.api

import akka.{Done, NotUsed}
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{OFormat, Json}

trait HelloWorldService extends Service {

  def hello(id: String): ServiceCall[Source[String, NotUsed], Response1234]

  override final def descriptor: Descriptor = {
    import Service._
    named("hello-world")
      .withCalls(
        pathCall("/api/hello/:id", hello _)
      )
  }
}

case class Request1234(message: String, length:Int, md5:String)

object Request1234 {
  implicit val format: OFormat[Request1234] = Json.format[Request1234]
}

case class Response1234(message: String)

object Response1234 {
  implicit val format: OFormat[Response1234] = Json.format[Response1234]
}
