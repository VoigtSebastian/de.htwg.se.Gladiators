package de.htwg.se.gladiators.model.json

import de.htwg.se.gladiators.model.Shop
import de.htwg.se.gladiators.model.json.GladiatorJson._

import play.api.libs.json._

case object ShopJson {
    implicit val shopWrites = new Writes[Shop] {
        def writes(shop: Shop) = Json.obj(
            "stock" -> shop.stock,
            "itemsInStock" -> shop.itemsInStock)
    }
}
