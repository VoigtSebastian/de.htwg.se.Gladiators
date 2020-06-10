package de.htwg.se.gladiators.playerModule

import akka.http.scaladsl.model.{ HttpEntity, HttpResponse, ContentTypes, MediaTypes }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Route, StandardRoute }
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import de.htwg.se.gladiators.util.{ UpdateNameArgumentContainer, BaseAttackedArgumentContainer }
import scala.util.Properties.envOrElse

import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.database.PlayerDatabase
import de.htwg.se.gladiators.database.relational.SlickDatabase
import spray.json._
import de.htwg.se.gladiators.util.JsonSupport

case class PlayerHttpServer() extends JsonSupport {

    private val database: PlayerDatabase = new SlickDatabase()
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route: Route = concat(
        /*
        //  GET ROUTES
        */
        get {
            path("gladiators" / "player" / "static") {
                complete("""{ "Gladiators": "online" }""".parseJson)
            }
        },
        get {
            path("gladiators" / "player" / "read") {
                complete(database.readPlayer())
            }
        },
        /*
        //  PUT ROUTES
        */
        put {
            path("gladiators" / "player" / "create") {
                entity(as[Player]) { player =>
                    database.createPlayer(player)
                    complete(params)
                }
            }
        },
        put {
            path("gladiators" / "player" / "updateName") {
                entity(as[UpdateNameArgumentContainer]) { params =>
                    println(params.player)
                    println(params.name)
                    var player = params.player.updateName(params.name)
                    complete(player)
                }
            }
        },
        put {
            path("gladiators" / "player" / "baseAttacked") {
                entity(as[BaseAttackedArgumentContainer]) { params =>
                    val player = params.player.baseAttacked(params.ap).toJson
                    complete(player)
                }
            }
        })

    val port = envOrElse("PLAYER-SERVICE-PORT", "8081").toInt
    val domain = envOrElse("DOMAIN", "localhost")

    val bindingFuture = Http().bindAndHandle(route, domain, port)
    println(s"Player Rest Service started on $domain $port")

    def shutdownWebServer(): Unit = {
        bindingFuture
            .flatMap(_.unbind()) // trigger unbinding from the port
            .onComplete(_ => system.terminate()) // and shutdown when done
    }
}
