package de.htwg.se.gladiators.playerModule

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, ContentTypes, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import de.htwg.se.gladiators.playerModule.controller.controllerComponent.PlayerControllerInterface;
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import de.htwg.se.gladiators.playerModule.util._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

case class PlayerHttpServer(controller: PlayerControllerInterface) extends PlayJsonSupport {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route: Route = concat(
 
        put {
            path("gladiators" / "player" / "updateName") {
                entity(as[UpdateNameArgumentContainer]) { params =>
                    println(params.player)
                    println(params.name)
                    var player = params.player.updateName(params.name)
                    val input = """{"uid":1,"txt":"#Akka rocks!"}""" + "\n" +
                        """{"uid":2,"txt":"Streaming is so hot right now!"}""" + "\n" +
                        """{"uid":3,"txt":"You cannot enter the same river twice."}"""
                    complete(HttpEntity(ContentTypes.`application/json`, input ))

                    //complete(controller.updateName(params.player, params.name))
                }
            } 
        },
        put {
            path("gladiators" / "player" / "baseAttacked") {
                entity(as[BaseAttackedArgumentContainer]) { params =>
                    var player = controller.baseAttacked(params.player, params.ap).toJson
                    complete(HttpEntity(ContentTypes.`application/json`, """{"name2 : "jo" , "credits"": 100, "baseHP" : 100, "boughtGladiator": true, "enemyBaseLine": 0}""" ))
                }
            }
        }
    )
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8102)

    println("Player Rest Service started on port 8102")

    def shutdownWebServer() : Unit = {
        bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
    }
}