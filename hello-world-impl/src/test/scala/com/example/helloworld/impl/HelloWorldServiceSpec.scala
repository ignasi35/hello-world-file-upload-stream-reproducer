package com.example.helloworld.impl

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{ BeforeAndAfterAll, AsyncWordSpec, Matchers }
import com.example.helloworld.api._
import play.api.libs.json._

class HelloWorldServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra(false)
      .withCluster(false)
  ) { ctx =>
    new HelloWorldApplication(ctx) with LocalServiceLocator
  }

  val client: HelloWorldService = server.serviceClient.implement[HelloWorldService]

  override protected def afterAll(): Unit = server.stop()

  "Hello World service" should {

    "say hello" in {
      val req = Request1234("message"*2, 565656, "fake-md5")
      val source: Source[String, NotUsed] = objToSource(req)
        .concat(Source.maybe)

      client.hello("Alice").invoke(source)
      .map { rsp =>
        println(s" - - - - - - - - - - - - - - - - - - - - - Stream response: ${rsp}")
        rsp shouldBe a[Response1234]
      }
    }

    def objToSource[T: OFormat](obj: T): Source[String, NotUsed] = {
      val str = Json.stringify(Json.toJsObject(obj))
//      println(s"* * * * * * * *  - $str")
      val cmdPayload = str.grouped(128).toList
      Source(cmdPayload)
    }
  }
}