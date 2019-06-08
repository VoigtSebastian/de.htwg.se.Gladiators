package de.htwg.se.gladiators.aview.gui

import de.htwg.se.gladiators.controller._
import de.htwg.se.gladiators.model.GladiatorType
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import javax.swing.SpringLayout.Constraints
import javax.swing._
import javax.swing.text.Position

import scala.swing._
import scala.swing.{Alignment, BorderPanel, Button, Dimension, Font, Frame, GridPanel, TextField}
import scala.swing.Swing.LineBorder
import scala.swing.event.ButtonClicked

class SwingGui(controller: Controller) extends MainFrame {

    preferredSize = new Dimension(590,800)
    title = "Gladiators"
    var cells = Array.ofDim[CellPanel](controller.playingField.size, controller.playingField.size)
    listenTo(controller)
    background = java.awt.Color.BLACK

    val gladPanel = new GridPanel(1,3) {

      val gladType= new TextField("TY:\nTest")
      gladType.font = new Font("Verdana", 1, 20)
      gladType.horizontalAlignment = Alignment.Center
      contents += gladType

      val gladAP= new TextField("AP:")
      gladAP.font = new Font("Verdana", 1, 20)
      gladAP.horizontalAlignment = Alignment.Center

      val gladHP= new TextField("HP:")
      gladHP.font = new Font("Verdana", 1, 20)
      gladHP.horizontalAlignment = Alignment.Center

      contents += gladType
      contents += gladAP
      contents += gladHP
    }
    val statusPanel = new GridPanel(1, 3) {
      val statusline = new TextField(controller.gameStatus.toString, 1)
      statusline.font = new Font("Verdana", 1, 30)
      statusline.horizontalAlignment = Alignment.Center

      val credits = new TextField(controller.players(controller.gameStatus.id).credits.toString + " $", 1)
      credits.font = new Font("Verdana", 1, 30)
      credits.foreground = java.awt.Color.GREEN.darker().darker()
      credits.horizontalAlignment = Alignment.Center

      val command = new TextField("", 1)
      command.font = new Font("Verdana", 1, 30)
      command.foreground = java.awt.Color.ORANGE.darker()
      command.horizontalAlignment = Alignment.Center

      contents += command
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
        listenTo(button_m)
        reactions += {
            case ButtonClicked(b) =>
              if (b == button_c) {
                controller.changeCommand(CommandStatus.CR)
                refreshStatus
              } else if (b == button_m) {
                controller.changeCommand(CommandStatus.MV)
                refreshStatus
              }

        }
    }

    val infoPanel = new GridPanel(3,1) {
      contents += gladPanel
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
        contents += new MenuItem(scala.swing.Action("New Map") {controller.createRandom()} )
        contents += new MenuItem(scala.swing.Action("Exit") {System.exit(0)})
      }
      pack
      visible = true
    }
    reactions += {
        case event: GladChanged => redraw()
        case event: PlayingFieldChanged => redraw()
        case event: CellClicked => initialize()
        case event: GameStatusChanged => refreshStatus
    }

    def redraw(): Unit = {
        for {
          line <- 0 until controller.playingField.size
          row <- 0 until controller.playingField.size
        } cells(line)(row).redraw
        showGladiators
        refreshStatus
        refreshGladPanel
        repaint
    }

    def initialize(): Unit = {
        for {
            line <- 0 until controller.playingField.size
            row <- 0 until controller.playingField.size
        } cells(line)(row).initialize
        showGladiators
        refreshStatus
        refreshGladPanel
        repaint
    }

    def refreshStatus: Unit = {
        statusPanel.statusline.text = "" + controller.players(controller.gameStatus.id).name
        statusPanel.command.text = "" + CommandStatus.message(controller.commandStatus)
        statusPanel.credits.text = "" + controller.players(controller.gameStatus.id).credits.toString + " $"
      //changePlayerNames()
    }

    def refreshGladPanel: Unit = {
        if (controller.checkGladiator(controller.selectedCell._1, controller.selectedCell._2)) {
          val glad = controller.getGladiator(controller.selectedCell._1, controller.selectedCell._2)
          gladPanel.gladType.text = glad.gladiatorType.toString
          gladPanel.gladAP.text = "AP: " + glad.ap.toString
          gladPanel.gladHP.text = "HP: " + glad.hp.toString
        } else {
          gladPanel.gladType.text = ""
          gladPanel.gladAP.text = ""
          gladPanel.gladHP.text = ""
        }
    }

    def showGladiators: Unit = {
      for {
        glad <- controller.playingField.gladiatorPlayer1
      } cells(glad.line)(glad.row).setGlad(GameStatus.P1, glad)

      for {
        glad <- controller.playingField.gladiatorPlayer2
      } cells(glad.line)(glad.row).setGlad(GameStatus.P2, glad)

    }

    def changePlayerNames(): Unit = {
      val textArea = new JTextArea()
      textArea.setEditable(true)
      val scrollPane = new JScrollPane(textArea)
      scrollPane.requestFocus()
      textArea.requestFocusInWindow()
      scrollPane.setPreferredSize( new Dimension(800, 600))
      JOptionPane.showMessageDialog(textArea, "jo")
      val info = textArea.getText()
    }
}
