package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.Player
import org.scalatest.{Matchers, WordSpec}

class PlayerSpec extends WordSpec with Matchers{
  "A Player" when { "created new with no parameter" should {
    val player = Player()
    "have a name"  in {
      player.name should be("NO_NAME")
    }
    "have a nice String representation" in {
      player.toString should be("NO_NAME")
    }
  }}


  "A Player" when { "new" should {
      val player = Player("Your Name")
      "have a name"  in {
      player.name should be("Your Name")
    }
    "have a nice String representation" in {
      player.toString should be("Your Name")
    }
  }}
}
