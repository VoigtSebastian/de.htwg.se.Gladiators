package de.htwg.se.gladiators.playerService

import akka.http.scaladsl.Http
import scala.io.StdIn
import akka.actor.ActorSystem
import java.net.http.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.HttpRequest
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ HttpMethods, Uri, ContentTypes, HttpEntity, HttpResponse }
import akka.http.scaladsl.model.headers.RawHeader
import scala.concurrent.duration.{ Duration, SECONDS }
import scala.concurrent.{ Await, Future }
import akka.http.javadsl.model.ResponseEntity
import de.htwg.se.gladiators.playerService.routes.Routes

object PlayerService extends App {
    implicit val system = ActorSystem("checkout-system")
    implicit val executionContext = system.dispatcher

    Runtime.getRuntime.addShutdownHook(new Thread() { override def run = shutdown })

    val bindingFuture = Http().bindAndHandle(Routes.routes, "0.0.0.0", 8086)
    println(s"Server online at http://0.0.0.0:8086/")

    while (true) { Thread.sleep(500) }

    def shutdown = {
        bindingFuture
            .flatMap(_.unbind())
            .onComplete(_ => system.terminate())
        println("\nGoodbye")
    }
}
