package de.htwg.se.gladiators.aview.gui

import java.awt.GridBagConstraints

import de.htwg.se.gladiators.controller._
import de.htwg.se.gladiators.model.{Gladiator, GladiatorType}
import de.htwg.se.gladiators.model.GladiatorType.GladiatorType
import javax.swing.SpringLayout.Constraints
import javax.swing._
import javax.swing.text.Position

import scala.swing.{Alignment, BorderPanel, Button, Dimension, Font, Frame, GridPanel, TextField, _}
import scala.swing.Swing.LineBorder
import scala.swing.event.ButtonClicked

class SwingGui(controller: Controller) extends MainFrame {

    preferredSize = new Dimension(660,800)
    title = "Gladiators"
    var cells = Array.ofDim[CellPanel](controller.playingField.size, controller.playingField.size)
    listenTo(controller)
    background = java.awt.Color.BLACK

    val shopPanel = new GridBagPanel() {
      val header = new Label("SHOP")

      val glad1 = new Button("")
      val glad2 = new Button("")
      val glad3 = new Button("")
      val glad4 = new Button("")
      val glad5 = new Button("")

      val glad1_l = new Label("")
      val glad2_l = new Label("")
      val glad3_l = new Label("")
      val glad4_l = new Label("")
      val glad5_l = new Label("")

      header.font = new Font("Verdana", 1, 20)

      glad1_l.verticalAlignment = Alignment.Top

      add(header, constraints(0,0,1,1,0, 0.1))

      add(glad1, constraints(0,1))
      add(glad1_l, constraints(0,2, 1, 1, 0, 0.1))

      add(glad2, constraints(0,4))
      add(glad2_l, constraints(0,5, 1,1, 0, 0.1))

      add(glad3, constraints(0,7))
      add(glad3_l, constraints(0,8, 1, 1, 0, 0.1))

      add(glad4, constraints(0,10))
      add(glad4_l, constraints(0,11, 1, 1, 0, 0.1))

      add(glad5, constraints(0,13))
      add(glad5_l, constraints(0,14, 1, 1, 0, 0.1))


      listenTo(glad1, glad2, glad3, glad4, glad5)
      val glad_buttons: List[Button] = List(glad1, glad2, glad3, glad4, glad5)
      reactions += {
        case ButtonClicked(b) =>
          for ((c, i) <- glad_buttons.zipWithIndex) {
            if (c == b) {
              controller.selectedGlad = controller.shop.stock(i)
            }
          }
      }
      // Quelle : http://otfried.org/scala/index_42.html
      def constraints(x: Int, y: Int,
                      gridwidth: Int = 1, gridheight: Int = 1,
                      weightx: Double = 0.0, weighty: Double = 0.0,
                      fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None)
      : Constraints = {

        val c = new Constraints
        c.gridx = x
        c.gridy = y
        c.gridwidth = gridwidth
        c.gridheight = gridheight
        c.weightx = weightx
        c.weighty = weighty
        c.fill = fill
        c.anchor = GridBagPanel.Anchor.North
        c

      }
    }

    val gladPanel = new GridPanel(1,3) {

      val gladType= new TextField("TY:\nTest")
      gladType.font = new Font("Verdana", 1, 20)
      gladType.horizontalAlignment = Alignment.Center

      val gladAP= new Button("")
      gladAP.font = new Font("Algeria", 1, 20)
      //gladAP.background = java.awt.Color.CYAN.darker().darker().darker()
      gladAP.foreground = java.awt.Color.BLACK
      gladAP.horizontalAlignment = Alignment.Center

      val gladHP= new Button("")
      gladHP.font = new Font("Verdana", 1, 20)
      //gladHP.background = java.awt.Color.CYAN.darker().darker().darker()
      gladHP.foreground = java.awt.Color.BLACK
      gladHP.horizontalAlignment = Alignment.Center

     // contents += gladType
      contents += gladHP
      contents += gladAP
    }
    val statusPanel = new GridPanel(1, 3) {
      val statusline = new TextField(controller.gameStatus.toString, 1)
      statusline.font = new Font("Verdana", 1, 30)
      statusline.horizontalAlignment = Alignment.Center
      statusline.editable = false
      background = java.awt.Color.BLACK

      val credits = new TextField(controller.players(controller.gameStatus.id).credits.toString + " $", 1)
      credits.font = new Font("Dialog", 1, 25)
      credits.foreground = java.awt.Color.GREEN.darker().darker()
      credits.editable = false
      credits.horizontalAlignment = Alignment.Center

      val command = new TextField("", 1)
      command.font = new Font("Verdana", 1, 25)
      command.foreground = java.awt.Color.GREEN.darker().darker()
      command.editable = false
      command.horizontalAlignment = Alignment.Center

      contents += command
      contents += statusline
      contents += credits
    }

    val navPanel = new GridPanel(1,3) {
        preferredSize = new Dimension(700,50)
        val button_c = new Button("")
        val button_m = new Button("")
        val button_a = new Button("")

        button_c.icon = resizedTexture("textures/create.png", 300, 70)
        button_m.icon = resizedTexture("textures/move_blue.png", 300, 70)
        button_a.icon = resizedTexture("textures/attack_red.png", 300, 70)

        val button_glad_s = new Button("Sword")
        val button_glad_b = new Button("Bow")
        val button_glad_t = new Button("Tank")

        contents += button_c
        contents += button_m
        contents += button_a
        listenTo(button_c)
        listenTo(button_m)
        listenTo(button_a)
        reactions += {
            case ButtonClicked(b) =>
              if (b == button_c) {
                controller.changeCommand(CommandStatus.CR)
                for(i <- controller.baseArea(controller.players(controller.gameStatus.id))) {
                 // cells(i._1)(i._2).cell.border = LineBorder(java.awt.Color.GREEN, 7)
                  cells(i._1)(i._2).setHighlightedSand()
                }
                refreshStatus
              } else if (b == button_m) {
                controller.changeCommand(CommandStatus.MV)
                refreshStatus
                if (controller.checkGladiator(controller.selectedCell._1, controller.selectedCell._2)) {
                  val selectedGlad: Gladiator = controller.getGladiator(controller.selectedCell._1, controller.selectedCell._2)
                  for (i <- 0 to cells.length - 1) {
                    for (j <- 0 to cells.length - 1) {
                      if (controller.checkMovementPoints(selectedGlad, selectedGlad.line, selectedGlad.row, i, j))
                        if (controller.checkCellEmpty(i, j))
                          cells(i)(j).setHighlightedSand()
                      //cells(i)(j).cell.border = LineBorder(java.awt.Color.GREEN, 7)
                    }
                  }
                }
              } else if (b == button_a) {
                controller.changeCommand(CommandStatus.AT)
                refreshStatus
              }

        }
    }

    val infoPanel = new GridPanel(2,1) {
      contents += gladPanel
      contents += navPanel
      //contents += statusPanel
    }

    contents = new BorderPanel {
      add(shopPanel, BorderPanel.Position.West)
      add(statusPanel, BorderPanel.Position.North)
      add(gridPanel, BorderPanel.Position.Center)
      add(infoPanel, BorderPanel.Position.South)
    }

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
        contents += new MenuItem(scala.swing.Action("New Map") {controller.createRandom(7, 17)} )
          contents += new MenuItem(scala.swing.Action("Playernames") {
            var nameInput = JOptionPane.showInputDialog(
              null,
              "Player One",
              "Change Names",
              JOptionPane.QUESTION_MESSAGE
            )
            controller.players(0).name = nameInput
            nameInput = JOptionPane.showInputDialog(
              null,
              "Player Two",
              "Change Names",
              JOptionPane.QUESTION_MESSAGE
            )
            controller.players(1).name = nameInput
            refreshStatus
          })
        contents += new MenuItem(scala.swing.Action("Exit") {System.exit(0)})

          def createTextareaWidgetInsideScrollPane(text: String): JScrollPane = {
            val textArea = new JTextArea(1, 20)
            textArea.setText(text)
            textArea.setCaretPosition(0)
            textArea.setEditable(true)
            new JScrollPane(textArea)
          }
      }
      pack
      visible = true
    }

    reactions += {
        case event: GladChanged => redraw()
        case event: PlayingFieldChanged => redraw()
        case event: CellClicked => redraw()
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
        showShop
        repaint
    }
/*
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
*/
    def refreshStatus: Unit = {
        statusPanel.command.text = "" + CommandStatus.message(controller.commandStatus)
        statusPanel.credits.text = "" + controller.players(controller.gameStatus.id).credits.toString + " $"
        statusPanel.statusline.text = "" + controller.players(controller.gameStatus.id).name
        if (controller.gameStatus.id == 0)
          statusPanel.statusline.foreground = java.awt.Color.BLUE.darker()
        else
          statusPanel.statusline.foreground = java.awt.Color.RED.darker()
    }

    def refreshGladPanel: Unit = {
        if (controller.checkGladiator(controller.selectedCell._1, controller.selectedCell._2)) {
          val glad = controller.getGladiator(controller.selectedCell._1, controller.selectedCell._2)
          gladPanel.gladType.text = glad.gladiatorType.toString
          gladPanel.gladAP.text =  glad.ap.toString
          gladPanel.gladHP.text =  glad.hp.toString
          gladPanel.gladHP.icon = resizedTexture("textures/hp_black.png", 40, 40)
          gladPanel.gladAP.icon = resizedTexture("textures/ap_black.png", 40, 40)
        } else {
          gladPanel.gladType.text = ""
          gladPanel.gladAP.text = ""
          gladPanel.gladHP.text = ""
          gladPanel.gladAP.icon = new ImageIcon("disable")
          gladPanel.gladHP.icon = new ImageIcon("disable")
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

    def showShop: Unit = {
      // shopPanel.glad1.icon
      for ((g, i) <- controller.shop.stock.zipWithIndex) {
        i match {
          case 0 => shopPanel.glad1.icon = getGladIcon(g)
            shopPanel.glad1_l.text = "<html><body>HP: " + g.hp + "<br>AP: " + g.ap + "</body></html>"
          case 1 => shopPanel.glad2.icon = getGladIcon(g)
            shopPanel.glad2_l.text = "<html><body>HP: " + g.hp + "<br>AP: " + g.ap + "</body></html>"
          case 2 => shopPanel.glad3.icon = getGladIcon(g)
            shopPanel.glad3_l.text = "<html><body>HP: " + g.hp + "<br>AP: " + g.ap + "</body></html>"
          case 3 => shopPanel.glad4.icon = getGladIcon(g)
            shopPanel.glad4_l.text = "<html><body>HP: " + g.hp + "<br>AP: " + g.ap + "</body></html>"
          case 4 => shopPanel.glad5.icon = getGladIcon(g)
            shopPanel.glad5_l.text = "<html><body>HP: " + g.hp + "<br>AP: " + g.ap + "</body></html>"
          case _ =>
        }
      }
      //--------------------

      // new map geht nicht


      ///----------------
    }

    def getGladIcon(glad: Gladiator): ImageIcon = {
      glad.gladiatorType match {
        case GladiatorType.SWORD
        => resizedTexture("textures/sword.png", 40, 40)
        case GladiatorType.BOW
        => resizedTexture("textures/bow.png", 40, 40)
        case GladiatorType.TANK
        => resizedTexture("textures/axe.png", 40, 40)
      }
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

  def resizedTexture(path:String, width:Int, height:Int): ImageIcon = {
    var imageIcon = new ImageIcon(path)
    var image = imageIcon.getImage; // transform it
    var newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    imageIcon = new ImageIcon(newimg);  // transform it back
    imageIcon
  }

}
