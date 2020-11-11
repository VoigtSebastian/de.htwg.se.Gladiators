package de.htwg.se.gladiators.model

import de.htwg.se.gladiators.model.GladiatorType.Archer
import de.htwg.se.gladiators.model.TileType.Mine

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TileTypeSpec extends AnyWordSpec with Matchers {
    "Every TileType" should {
        "have a colored string representation without gladiator" in {
            TileType.values.foreach(_.coloredStringRepresentation(None) should not be (empty))
        }
        "have a colored string representation with gladiator" in {
            TileType.values.foreach(_.coloredStringRepresentation(Some(Archer)) should contain('A'))
        }
        "have a simple string representation without gladiator" in {
            TileType.values.foreach(_.stringRepresentation(None) should not be (empty))
        }
        "have a simple string representation with gladiator" in {
            TileType.values.foreach(_.stringRepresentation(Some(Archer)) should contain('A'))
        }
    }
    "A Mine" should {
        "have a gold per hit value" in {
            Mine(10).goldPerHit should be > 0
        }
        "have a mine function that returns None, because it was empty" in {
            Mine(0).mine should be(None)
            Mine(-1).mine should be(None)
            Mine(-5).mine should be(None)
        }
        "have a mine function that returns an updated mine reduces by goldPerHit" in {
            val mine = Mine(100)
            mine.mine should be(Some(Mine(100 - mine.goldPerHit)))
        }
        "have a mine function that returns None, because it is depleted" in {
            val mine = Mine(1)
            mine.mine should be(None)
        }
    }
}
