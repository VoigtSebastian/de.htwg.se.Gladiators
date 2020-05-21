package de.htwg.se.gladiators.aview
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.stream.ActorMaterializer
import de.htwg.se.gladiators.controller.controllerComponent.{ControllerInterface, GladChanged, PlayingFieldChanged}
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration.FiniteDuration
import akka.stream.scaladsl._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.Http
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

class HttpServer(controller: ControllerInterface) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route: Route = get {
   
        path("gladiators/playingfield") {
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, playingFieldToHtml))
        }
        path("gladiators" / Segment) { 
            command => {
                processInputLine(command)
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, command + "</br>" + playingFieldToHtml))
            }
        }
    }
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)


    def playingFieldToHtml: String = {
        // todo: send playingfield as html or json
        "<p>PlayingField</p>"
    }

    def unbind(): Unit = {
        bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
    }

    def processInputLine(command: String): Unit = {
        // todo: validate input here and call controller (similar to TUI)
        controller.nextPlayer()
    }

}