package de.htwg.se.gladiators.aview.gui

import de.htwg.se.gladiators.controller._
import de.htwg.se.gladiators.model.GladiatorType
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import javax.swing.SpringLayout.Constraints
import javax.swing.{JButton, JTextField, SwingConstants}
import javax.swing.text.Position

import scala.swing._
import scala.swing.{Alignment, BorderPanel, Button, Dimension, Font, Frame, GridPanel, TextField}
import scala.swing.Swing.LineBorder
import scala.swing.event.ButtonClicked

class SwingGui(controller: Controller) extends MainFrame {

    preferredSize = new Dimension(590,750)
    title = "Gladiators"
    var cells = Array.ofDim[CellPanel](controller.playingField.size, controller.playingField.size)
    listenTo(controller)
    background = java.awt.Color.BLACK

    val statusPanel = new GridPanel(1, 3) {
      val statusline = new TextField(controller.gameStatus.toString, 1)
      statusline.font = new Font("Verdana", 1, 30)
      statusline.horizontalAlignment = Alignment.Center

      val credits = new TextField(controller.players(controller.gameStatus.id).credits.toString + " $", 1)
      credits.font = new Font("Verdana", 1, 30)
      credits.foreground = java.awt.Color.GREEN.darker().darker()
      credits.horizontalAlignment = Alignment.Center

      val coordinates = new TextField("", 1)
      coordinates.font = new Font("Verdana", 1, 30)
      coordinates.horizontalAlignment = Alignment.Center

      contents += coordinates
      contents += statusline
      contents += credits
    }

    val navPanel = new GridPanel(1,2) {
        preferredSize = new Dimension(700,50)
        val button_c = new Button("Create")
        val button_m = new Button("Move")

      button_c.font = new Font("Verdana", 0, 30)
      button_m.font = new Font("Verdana", 0, 30)

        contents += button_c
        contents += button_m
        listenTo(button_c)
        reactions += {
            case ButtonClicked(b)
              if b == button_c =>
                controller.addGladiator(controller.selectedCell._1, controller.selectedCell._2, GladiatorType.SWORD)
            case ButtonClicked(b)
              if b == button_m =>
                controller.addGladiator(controller.selectedCell._1, controller.selectedCell._2, GladiatorType.BOW)
        }

    }

    val infoPanel = new GridPanel(2,1) {
      contents += navPanel
      contents += statusPanel
    }

    contents = new BorderPanel {
      add(gridPanel, BorderPanel.Position.Center)
      add(infoPanel, BorderPanel.Position.South)
    }

  /*
    contents = new BorderPanel {
        add(gridPanel, BorderPanel.Position.Center)
        add(navPanel, BorderPanel.Position.Center)
        add(statusline, BorderPanel.Position.South)
    }
    */

   // def gridPanel : BoxPanel = new BoxPanel(Orientation.NoOrientation)  {
    def gridPanel: GridPanel = new GridPanel(controller.playingField.size, controller.playingField.size) {
        border = LineBorder(java.awt.Color.BLACK, 3)
        background = java.awt.Color.BLACK
        for {
          line <- 0 until controller.playingField.size
          row <-  0 until controller.playingField.size
        } {
            val cellPanel = new CellPanel(line, row, controller)
            cells(line)(row) = cellPanel
            contents += cellPanel
            listenTo(cellPanel)
        }
    }

    redraw()
    visible = true

    menuBar = new MenuBar {

      contents += new Menu("Menu") {
        contents += new MenuItem(Action("New Game") {controller.createRandom()} )
      }
      pack
      visible = true
    }
    reactions += {
        case event: GladChanged => redraw()
        case event: PlayingFieldChanged => redraw()
        case event: CellClicked => initialize()
        case event: GameStatusChanged => refreshStatus()
    }

    def redraw(): Unit = {
        for {
          line <- 0 until controller.playingField.size
          row <- 0 until controller.playingField.size
        } cells(line)(row).redraw
        showGladiators()
        refreshStatus()
        repaint
    }

    def initialize(): Unit = {
        for {
            line <- 0 until controller.playingField.size
            row <- 0 until controller.playingField.size
        } cells(line)(row).initialize
        repaint
        showGladiators()
        refreshStatus()
    }

    def refreshStatus(): Unit = {
        statusPanel.statusline.text = "" + controller.players(controller.gameStatus.id).name
        statusPanel.coordinates.text = "" + controller.selectedCell
        statusPanel.credits.text = "" + controller.players(controller.gameStatus.id).credits.toString + " $"
    }

    def showGladiators(): Unit = {

      for {
        glad <- controller.playingField.gladiatorPlayer1
      } cells(glad.line)(glad.row).setGlad(GameStatus.P1, glad.gladiatorType)

      for {
        glad <- controller.playingField.gladiatorPlayer2
      } cells(glad.line)(glad.row).setGlad(GameStatus.P2, glad.gladiatorType)

    }

}
