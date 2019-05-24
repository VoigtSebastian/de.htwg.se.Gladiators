package de.htwg.se.gladiators.aview.gui

import de.htwg.se.gladiators.controller.{Controller, GladChanged, PlayingFieldChanged}
import javax.swing.{JTextField, SwingConstants}
import javax.swing.text.Position

import scala.swing.{Alignment, BorderPanel, Button, Dimension, Font, Frame, GridPanel, TextField}
import scala.swing.Swing.LineBorder

class SwingGui(controller: Controller) extends Frame {

  preferredSize = new Dimension(700,700)
  title = "Gladiators"
  var cells = Array.ofDim[CellPanel](controller.playingField.size, controller.playingField.size)
  listenTo(controller)
  background = java.awt.Color.GREEN

  val statusline = new TextField(controller.gameStatus.toString, 1)
  statusline.font = new Font("Verdana", 1, 30)
  statusline.horizontalAlignment = Alignment.Center
  contents = new BorderPanel {
    //add(navPanel, BorderPanel.Position.North)
    add(gridPanel, BorderPanel.Position.Center)
    add(statusline, BorderPanel.Position.South)
  }
/*
  def navPanel = new GridPanel(1,2) = {
    val button1 = new Button()
    // += button1
    //listenTo(button1)
  }
*/
  def gridPanel: GridPanel = new GridPanel(controller.playingField.size, controller.playingField.size) {
    border = LineBorder(java.awt.Color.BLACK, 3)
    background = java.awt.Color.BLACK
    for {
      row <- 0 until controller.playingField.size
      line <- 0 until controller.playingField.size
    } {

      val cellPanel = new CellPanel(row, line, controller)
      cells(row)(line) = cellPanel
      contents += cellPanel
      listenTo(cellPanel)
    }
  }

  redraw()
  visible = true

  reactions += {
    case event: GladChanged => redraw()
    case event: PlayingFieldChanged => redraw()
  }

  def redraw(): Unit = {
    for {
      row <- 0 until controller.playingField.size
      line <- 0 until controller.playingField.size
    } cells(row)(line).redraw
    repaint
  }


}
