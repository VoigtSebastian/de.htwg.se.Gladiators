package de.htwg.se.sudoku.aview

import de.htwg.se.gladiators.model.{Cell,Gladiator,Player,PlayingField}


class Tui {

  def processInputLine(input: String, pf:PlayingField):PlayingField = {

    input match {

      case "q" => pf
      case "n"=> pf.createRandom(7);
      //case "g" =>
      /*
       case _ => {

         input.toList.filter(c => c != ' ').map(c => c.toString.toInt) match {
           case row :: column :: value :: Nil => grid.set(row, column, value)
           case _ => grid
       */
      }

  }

}

