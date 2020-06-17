package de.htwg.se.gladiators.database.document

import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.database.PlayerDatabase

import org.mongodb.scala._
import org.mongodb.scala.bson.BsonValue

import scala.concurrent.duration.{ Duration, FiniteDuration }
import scala.concurrent.Await
import com.google.inject.{ Guice, Inject }

class MongoDb @Inject() extends PlayerDatabase {
    val DURATION: FiniteDuration = Duration.fromNanos(1000000000)

    val mongoClient: MongoClient = MongoClient()
    val database: MongoDatabase = mongoClient.getDatabase("gladiators")
    val playerCollection: MongoCollection[Document] = database.getCollection("players")

    def createPlayer(player: Player): Option[Player] = {
        try {
            Await.result(playerCollection.insertOne(
                Document("name" -> player.name, "credits" -> player.credits)).toFuture(), Duration.fromNanos(1000000000))
            println("Saved player in Mongo database")
            Some(player)
        } catch {
            case _: Throwable => None
        }
    }

    def readPlayers(): Seq[Player] = {
        val playerFuture = Await.result(playerCollection.find().toFuture(), Duration.Inf)
        val playerList: Seq[Player] = playerFuture
            .map(pl => {
                Player(
                    name = pl.get("name").getOrElse(return Seq()).asString().getValue,
                    credits = pl.get("credits").getOrElse(return Seq()).asInt32().getValue)
            })
        println(playerList)
        playerList
    }

    def updatePlayer(player: Player): Option[Player] = ???

    def deletePlayer(player: Player): Option[Player] = ???

}