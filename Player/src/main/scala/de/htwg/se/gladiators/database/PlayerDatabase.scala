package de.htwg.se.gladiators.database

import model.DeskInterface
import model.deskComp.deskBaseImpl.PlayerInterface

trait PlayerDataBase {

    def createPlayer(): Try[Boolean]

    def readPlayer(): Unit

    def updatePlayer(): Try[Boolean]

    def deletePlayer(): Try[Boolean]

}