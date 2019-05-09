package de.htwg.se.gladiators.model.model

import de.htwg.se.gladiators.Gladiators
import org.scalatest.{Matchers, WordSpec}

class GladiatorsSpec extends WordSpec with Matchers {
  "The Sudoku main class" should {
    "accept text input as argument without readline loop, to test it from command line " in {
      Gladiators.main(Array[String]("test"))
    }
  }
}
