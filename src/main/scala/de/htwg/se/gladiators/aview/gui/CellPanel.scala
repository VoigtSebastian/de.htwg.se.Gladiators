package de.htwg.se.gladiators.aview.gui

import java.io.File

import scala.swing._
import javax.swing.table._

import scala.swing.event._
import de.htwg.se.gladiators.controller.controllerComponent.GameStatus.GameStatus
import de.htwg.se.gladiators.controller.controllerComponent.{ControllerInterface, GameStatus}
import de.htwg.se.gladiators.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.gladiators.model.{Cell, Gladiator, GladiatorType}
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import javax.swing.ImageIcon
//import de.htwg.se.gladiators.controller.CellChanged
import scala.swing.Swing.LineBorder

//class CellPanel(line: Int, row: Int, controller: Controller) extends FlowPanel {
class CellPanel(line: Int, row: Int, controller: ControllerInterface, val dimWidth: Int, val dimHeight: Int) extends GridPanel(1, 1) {

    preferredSize = new Dimension(50, 150)
    val givenCellColor = new Color(200, 200, 255)
    val cellColor = new Color(224, 224, 255)
    val highlightedCellColor = new Color(192, 255, 192)
    background = java.awt.Color.WHITE

    val sword_p1 = resizedTexture("textures/sandsword_small_p1.png", dimHeight, dimHeight)
    val bow_p1 = resizedTexture("textures/sandbow_small_p1.png", dimWidth, dimHeight)
    val shield_p1 = resizedTexture("textures/sandshield_small_p1.png", dimWidth, dimHeight)
    val sword_p2 = resizedTexture("textures/sandsword_small_p2.png", dimHeight, dimHeight)
    val bow_p2 = resizedTexture("textures/sandbow_small_p2.png", dimWidth, dimHeight)
    val shield_p2 = resizedTexture("textures/sandshield_small_p2.png", dimWidth, dimHeight)

    val sand =  resizedTexture("textures/sand_60.png", dimWidth, dimHeight)
    val palm = resizedTexture("textures/palmsand_60_color.png", dimWidth, dimHeight)
    val gold = resizedTexture("textures/sandgold_small.png", dimWidth, dimHeight)

    def myCell: Cell = controller.cell(line, row)

    val label: Label =
        new Label {
            // text = getCellText
            foreground = java.awt.Color.YELLOW
            font = new Font("Verdana", 1, 10)
            //horizontalAlignment = Alignment.Center
        }

    val hp: Label =
        new Label {
            text = getCellText
            foreground = java.awt.Color.WHITE
            font = new Font("Verdana", 1, 12)
            // verticalAlignment = Alignment.Bottom
        }

    //val cell: GridPanel = new GridPanel(1,1) {
    val cell: BorderPanel = new BorderPanel() {
        add(label, BorderPanel.Position.Center)
        add(hp, BorderPanel.Position.South)
        //contents += label
        //contents += hp

        background = getCellColor
        preferredSize = new Dimension(80, 80)
        //background = if (controller.isGiven(row, column)) givenCellColor else cellColor
        //border = Swing.BeveledBorder(Swing.Raised)
        listenTo(mouse.clicks)
        listenTo(controller)

        reactions += {
            case MouseClicked(src, pt, mod, clicks, pops) =>
                //controller.showCandidates(row, column)
                controller.cellSelected(line, row)
                border = LineBorder(java.awt.Color.GREEN.darker(), 4)
        }
    }

    def redraw: Unit = {
        contents.clear()
        contents += cell
        cell.background = getCellColor
        cell.border = LineBorder(java.awt.Color.BLACK, 1)
        setCellTexture
        hp.text = getCellText
        //label.text = getCellText
        repaint
    }

    def getCellColor: java.awt.Color = {
        var color = java.awt.Color.BLACK
        myCell.cellType.id match {
            case 0 => color = java.awt.Color.BLACK
            case 1 => color = java.awt.Color.BLACK
            case 2 =>
                if (line == 0)
                    color = java.awt.Color.RED
                else
                    color = java.awt.Color.BLUE.darker()
            case 3 => color = java.awt.Color.BLACK
        }
        color
    }

    def getCellText: String = {
        var str = ""
        myCell.cellType.id match {
            case 0 => str = ""
            case 1 => str = ""
            case 2 =>
                if (line == 0)
                    str = controller.players(1).baseHP.toString
                else
                    str = controller.players(0).baseHP.toString
            case 3 => str = ""
        }
        str
    }

    def setCellTexture: Unit = {
        myCell.cellType.id match {
            case 0 => label.icon = sand
            case 1 => label.icon = palm
            case 2 =>
                if (line == 0)
                    label.icon = resizedTexture("textures/sandcolloseum_small.png", dimWidth, dimHeight - 10)
                else
                    label.icon = resizedTexture("textures/sandtemple_small.png", dimWidth, dimHeight - 10)
            case 3 => label.icon = gold
        }
    }

    def setGlad(gameStatus: GameStatus, glad: Gladiator): Unit = {
        //label.text = getGladText(glad)
        if (gameStatus == GameStatus.P1) {
            //cell.background = java.awt.Color.CYAN
            glad.gladiatorType match {
                case GladiatorType.SWORD =>
                    label.icon = sword_p1
                case GladiatorType.BOW =>
                    label.icon = bow_p1
                case GladiatorType.TANK =>
                    label.icon = shield_p1
            }
        } else {
            //cell.background = java.awt.Color.PINK
            glad.gladiatorType match {
                case GladiatorType.SWORD =>
                    label.icon = sword_p2
                case GladiatorType.BOW =>
                    label.icon = bow_p2
                case GladiatorType.TANK =>
                    label.icon = shield_p2
            }
        }
    }

    def setHighlightedSand(): Unit = {
        label.icon = resizedTexture("textures/sand_small_green.png", dimHeight, dimWidth)
    }

    def getGladText(glad: Gladiator): String = {
        glad.hp.toString
    }

    def resizedTexture(path: String, width: Int, height: Int): ImageIcon = {
        var imageIcon = new ImageIcon(path)
        var image = imageIcon.getImage; // transform it
        var newimg = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg); // transform it back
        imageIcon
    }
}