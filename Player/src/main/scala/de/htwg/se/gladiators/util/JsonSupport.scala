package de.htwg.se.gladiators.util

import de.htwg.se.gladiators.model.Player

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val playerFormat = jsonFormat5(Player.apply)
  implicit val updateNameArgumentContainerFormat = jsonFormat2(UpdateNameArgumentContainer.apply)
  implicit val baseAttackedArgumentContainerFormat = jsonFormat2(BaseAttackedArgumentContainer.apply)
}
