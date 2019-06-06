package de.htwg.se.gladiators.model.model.util

import de.htwg.se.gladiators.util.{Observable, Observer}
import org.scalatest.{Matchers, WordSpec}

class ObservableSpec extends WordSpec with Matchers {
  "An Observable" should {
    val observable = new Observable
    val observer = new Observer {
      var updated: Boolean = false

      def isUpdated: Boolean = updated

      override def update: Unit = {
        updated = true; //updated
      }
    }
    "add an Observer" in {
      observable.add(observer)
      observable.subscribers should contain(observer)
    }
    "notify an Observer" in {
      observer.isUpdated should be(false)
      observable.notifyObservers
      observer.isUpdated should be(true)
    }
    "remove an Observer" in {
      observable.remove(observer)
      observable.subscribers should not contain observer
    }

  }
}
