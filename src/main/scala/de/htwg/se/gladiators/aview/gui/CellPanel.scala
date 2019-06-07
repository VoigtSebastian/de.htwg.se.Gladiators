package de.htwg.se.gladiators.aview.gui

import java.io.File

import scala.swing._
import javax.swing.table._

import scala.swing.event._
import de.htwg.se.gladiators.controller.{Controller, GameStatus}
import de.htwg.se.gladiators.controller.GameStatus.GameStatus
import de.htwg.se.gladiators.model.Cell
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
//import de.htwg.se.gladiators.controller.CellChanged
import scala.swing.Swing.LineBorder

class CellPanel(line: Int, row: Int, controller: Controller) extends FlowPanel {

  val givenCellColor = new Color(200, 200, 255)
  val cellColor = new Color(224, 224, 255)
  val highlightedCellColor = new Color(192, 255, 192)

  def myCell = controller.cell(line, row)

  val label =
    new Label {
      //text = cellText(row, column)
      text = getCellText
      font = new Font("Verdana", 0, 36)
      horizontalAlignment = Alignment.Center
    }

  val cell = new BorderPanel() {
    add(label, BorderPanel.Position.Center)
    background = getCellColor
    preferredSize = new Dimension(80, 80)
    //background = if (controller.isGiven(row, column)) givenCellColor else cellColor
    //border = Swing.BeveledBorder(Swing.Raised)
    listenTo(mouse.clicks)
    listenTo(controller)

    reactions += {
      /*controller.showCandidates(row, column)

      case e: CellChanged => {
        label.text = cellText(row, column)
        repaint
      }
      */
      case MouseClicked(src, pt, mod, clicks, pops) =>
        //controller.showCandidates(row, column)
        controller.cellSelected(line, row)
        border = LineBorder(java.awt.Color.RED, 4)
        //background = java.awt.Color.ORANGE
    }
  }

  def redraw: Unit = {
    contents.clear()
    contents += cell
    cell.background = getCellColor
    cell.border = LineBorder(java.awt.Color.BLACK,3)
    label.text = getCellText
    repaint
  }

  def initialize: Unit = {
    label.text = getCellText
    cell.background = getCellColor
    cell.border = LineBorder(java.awt.Color.BLACK,3)
  }

  def getCellColor: java.awt.Color = {
    var color = java.awt.Color.BLACK
    myCell.cellType.id match {
      case 0 => color = java.awt.Color.YELLOW
      case 1 => color = java.awt.Color.GREEN
      case 2 => color = java.awt.Color.GRAY
    }
    color
  }

  def getCellText: String = {
    var str = ""
    myCell.cellType.id match {
      case 0 => str = ""
      case 1 => str = ""
      case 2 => str = "H"
    }
    str
  }

  def setGlad(gameStatus: GameStatus, gladtype: GladiatorType): Unit = {
    label.text = getGladText(gladtype)
    if(gameStatus == GameStatus.P1) {
      cell.background = java.awt.Color.CYAN
    } else {
      cell.background = java.awt.Color.PINK
    }
  }

  def getGladText(gladtype: GladiatorType) : String = {
    var str = ""
    gladtype.id match {
      case 0 => str = "" + "S"
      case 1 => str = "" + "B"
      case 2 => str = "" + "T"
    }
    str
  }
}