package de.htwg.se.gladiators
import de.htwg.se.gladiators.controller.TuiController

object Gladiators {

    def main(args: Array[String]): Unit = {
        val tuiC = new TuiController
        tuiC.gameLoop()
    }
}
