package com.example.helloworld.impl

import akka.stream.scaladsl.Source
import akka.NotUsed
import com.example.helloworld.api
import com.example.helloworld.api._
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

class HelloWorldServiceImpl() extends HelloWorldService {

  override   def hello(id: String): ServiceCall[Source[String, NotUsed], Response1234]  = ServiceCall { _ =>
    Future.successful(Response1234(id))
  }

}
