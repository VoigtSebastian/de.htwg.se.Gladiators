package de.htwg.se.gladiators.model.model.controller

import de.htwg.se.gladiators.controller.MoveGladiatorCommand
import de.htwg.se.gladiators.controller.Controller
import de.htwg.se.gladiators.model.PlayingField
import org.scalatest.{Matchers, WordSpec}

class MoveGladiatorCommandSpec extends WordSpec with Matchers {
  var controller = new Controller(PlayingField())
  controller.createRandom(7, 0)
  controller.addGladiator(controller.playingField.size - 2, controller.playingField.size / 2)
  "A MoveGladiatorcommand" when {
    "create a move command" in {
      val command = new MoveGladiatorCommand(0, 0, 0, 0, controller)

    }
  }
}
