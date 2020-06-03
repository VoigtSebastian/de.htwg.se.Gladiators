package de.htwg.se.gladiators.model.fileIoComponent

import de.htwg.se.gladiators.model.playingFieldComponent.PlayingFieldInterface

trait FileIOInterface {

    def load: PlayingFieldInterface
    def save(playingField: PlayingFieldInterface): Unit

}