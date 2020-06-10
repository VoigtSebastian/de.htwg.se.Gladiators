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

object Mappings {

    // the base query for the Users table
    val players = TableQuery[Players]

    val db = Database.forURL(
        url = "jdbc:mysql://localhost:3306",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "root")

    Await.result(db.run(DBIO.seq(
        players.schema.createIfNotExists)), Duration.Inf)

    def createPlayer(player: Player): Boolean = {
        try {
            Await.result(db.run(DBIO.seq(
                players += DbPlayer(player.name))), Duration.Inf)
            true
        } catch {
            case err: Exception =>
                println("Error in database", err)
                false;
        }
        //    finally db.close
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

case class DbPlayer(name: String, id: Option[Int] = None)

class Players(tag: Tag) extends Table[DbPlayer](tag, "PLAYERS") {
    // Auto Increment the id primary key column
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    // The name can't be null
    def name = column[String]("NAME")

    // the * projection (e.g. select * ...) auto-transforms the tupled
    // column values to / from a User
    def * = (name, id.?) <> (DbPlayer.tupled, DbPlayer.unapply)
}
