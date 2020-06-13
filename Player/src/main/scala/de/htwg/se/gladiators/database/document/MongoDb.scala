package de.htwg.se.gladiators.database.document

import de.htwg.se.gladiators.model.Player
import de.htwg.se.gladiators.database.PlayerDatabase

import org.mongodb.scala._
import org.mongodb.scala.bson.BsonValue

import scala.concurrent.duration.{ Duration, FiniteDuration }
import scala.concurrent.Await

class MongoDb extends PlayerDatabase {
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
        try {
            val playerFuture = Await.result(playerCollection.find().toFuture(), Duration.Inf)
            val playerList = playerFuture
                .map(pl => Player(pl.get("name").toString(), pl.get("credits").toString().toInt))
            println(playerList)
            playerList
        } catch {
            case _:
                Throwable =>
                println("Something went wrong")
                List()
        }
    }

    def updatePlayer(player: Player): Option[Player] = ???

    def deletePlayer(player: Player): Option[Player] = ???

}