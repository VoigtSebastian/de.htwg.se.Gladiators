package de.htwg.se.gladiators.database.relational

import de.htwg.se.gladiators.model.Player

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import slick.jdbc.MySQLProfile.api._
import slick.jdbc.JdbcBackend.Database

import scala.collection.immutable.SortedSet
import scala.util.{ Try, Success, Failure }

//todo add when intellij changes imports
//import scala.concurrent.Await
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.duration.Duration
//import slick.driver.H2Driver.api._

object PlayerMappings {

    // the base query for the Users table
    val players = TableQuery[Players]

    val db = Database.forURL(
        url = "jdbc:mysql://localhost:3306",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "root")

    Await.result(db.run(DBIO.seq(
        players.schema.create)), Duration.Inf)

    def createPlayer(player: Player): Boolean = {
        try {
            Await.result(db.run(DBIO.seq(
                players += DbPlayer(player.name, player.credits))), Duration.Inf)
            true
        } catch {
            case err: Exception =>
                println("Error in database", err)
                false;
        }
    }

    def readPlayer(): Option[Player] = {
        var player: Option[Player] = None
        Await.result(db.run(DBIO.seq(
            players.result.map(pl => {
                println(pl)
                player = Some(Player(pl.head.name))
            }))), Duration.Inf)
        player
    }

}

case class DbPlayer(name: String, credits: Int)

class Players(tag: Tag) extends Table[DbPlayer](tag, "PLAYERS") {
    // The name can't be null
    def name = column[String]("NAME", O.PrimaryKey)

    def credits = column[Int]("CREDITS")
    // the * projection (e.g. select * ...) auto-transforms the tupled
    // column values to / from a User
    def * = (name, credits) <> (DbPlayer.tupled, DbPlayer.unapply)
}
