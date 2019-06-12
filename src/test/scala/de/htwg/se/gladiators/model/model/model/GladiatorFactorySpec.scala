package de.htwg.se.gladiators.model.model.model

import org.scalatest.{Matchers, WordSpec}

import de.htwg.se.gladiators.model.{GladiatorType, GladiatorFactory, Player}

class GladiatorFactorySpec extends WordSpec with Matchers  {
    "The GladiatorFactory called with a GladiatorType of bow " when {
        val gladBow = GladiatorFactory.createGladiator(0, 0, GladiatorType.BOW, new Player) should have
        "a string representation of " in {
            gladBow.toString()
        }
    }

    "The GladiatorFactory called with a GladiatorType of sword " when {
        val gladBow = GladiatorFactory.createGladiator(0, 0, GladiatorType.SWORD, new Player) should have
        "a string representation of " in {
            gladBow.toString()
        }
    }

    "The GladiatorFactory called with a GladiatorType of tank " when {
        val gladBow = GladiatorFactory.createGladiator(0, 0, GladiatorType.TANK, new Player) should have
        "a string representation of " in {
            gladBow.toString()
        }
    }
}