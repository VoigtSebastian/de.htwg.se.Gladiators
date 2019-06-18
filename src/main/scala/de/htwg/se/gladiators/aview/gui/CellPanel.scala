package de.htwg.se.gladiators.aview.gui

import java.io.File

import scala.swing._
import javax.swing.table._

import scala.swing.event._
import de.htwg.se.gladiators.controller.{Controller, GameStatus}
import de.htwg.se.gladiators.controller.GameStatus.GameStatus
import de.htwg.se.gladiators.model.{Cell, Gladiator, GladiatorType}
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import javax.swing.ImageIcon
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
      font = new Font("Verdana", 0, 5)
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
        border = LineBorder(java.awt.Color.MAGENTA.darker().darker(), 6)

        //background = java.awt.Color.ORANGE
    }
  }

  def redraw: Unit = {
    contents.clear()
    contents += cell
    cell.background = getCellColor
    cell.border = LineBorder(java.awt.Color.BLACK,3)
    setCellTexture
    //label.text = getCellText
    repaint

  }

  def initialize: Unit = {
    setCellTexture
    //label.text = getCellText
    cell.background = getCellColor
    cell.border = LineBorder(java.awt.Color.BLACK,3)
  }

  def getCellColor: java.awt.Color = {
    var color = java.awt.Color.BLACK
    myCell.cellType.id match {
      case 0 => color = java.awt.Color.YELLOW
      case 1 => color = java.awt.Color.YELLOW.darker()
      case 2 =>
        if (line == 0)
          color = java.awt.Color.RED
        else
          color = java.awt.Color.BLUE.darker()
      case 3 => color = java.awt.Color.YELLOW.darker()
    }
    color
  }

  def getCellText: String = {
    var str = ""
    myCell.cellType.id match {
      case 0 => str = ""
      case 1 => str = ""
      case 2 => str = ""

    }
    str
  }

  def setCellTexture: Unit = {
    myCell.cellType.id match {
      case 0 => label.icon = resizedTexture("textures/sand_small.png", 90, 90)
      case 1 => label.icon = resizedTexture("textures/palmsand_small_color.png", 68, 68)
      case 2 => label.icon = resizedTexture("textures/housesand_small.png", 65, 65)
      case 3 => label.icon = resizedTexture("textures/hp.png", 65, 65)
    }
  }

  def setGlad(gameStatus: GameStatus, glad: Gladiator): Unit = {
    //label.text = getGladText(glad)
    if(gameStatus == GameStatus.P1) {
      //cell.background = java.awt.Color.CYAN
      glad.gladiatorType match {
        case GladiatorType.SWORD =>
          label.icon = resizedTexture("textures/sandsword_small_p1.png", 80, 80)
        case GladiatorType.BOW =>
          label.icon = resizedTexture("textures/sandbow_small_p1.png", 80, 80)
        case GladiatorType.TANK =>
          label.icon = resizedTexture("textures/sandaxe_small_p1.png", 80, 80)
      }
    } else {
      //cell.background = java.awt.Color.PINK
      glad.gladiatorType match {
        case GladiatorType.SWORD =>
          label.icon = resizedTexture("textures/sandsword_small_p2.png", 80, 80)
        case GladiatorType.BOW =>
          label.icon = resizedTexture("textures/sandbow_small_p2.png", 80, 80)
        case GladiatorType.TANK =>
          label.icon = resizedTexture("textures/sandaxe_small_p2.png", 80, 80)
      }
    }
  }

  def getGladText(glad: Gladiator) : String = {
    glad.hp.toString
  }

  def resizedTexture(path:String, width:Int, height:Int): ImageIcon = {
    var imageIcon = new ImageIcon(path)
    var image = imageIcon.getImage; // transform it
    var newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    imageIcon = new ImageIcon(newimg);  // transform it back
    imageIcon
  }
}