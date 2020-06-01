package de.htwg.se.gladiators.aview.gui

import de.htwg.se.gladiators.controller.controllerComponent._
import de.htwg.se.gladiators.model.{Gladiator, GladiatorType}
import de.htwg.se.gladiators.util.{Coordinate}
import javax.swing._

import scala.swing.{Alignment, BorderPanel, Button, Dimension, Font, Frame, GridPanel, TextField, _}
import scala.swing.Swing.LineBorder
import scala.swing.event.{ButtonClicked, MouseClicked}

class SwingGui (controller: ControllerInterface) extends MainFrame {

    preferredSize = new Dimension(850, 950)
    title = "Gladiators"
    var cells: Array[Array[CellPanel]] = Array.ofDim[CellPanel](controller.playingField.size, controller.playingField.size)
    listenTo(controller)
    background = java.awt.Color.WHITE

    iconImage = toolkit.getImage("textures/gladLogo_small.png")
    // resizedTexture("textures/shield_small.png", 50, 50)
    val shopPanel = new GridBagPanel() {
        preferredSize = new Dimension(80, 400)
        background = java.awt.Color.WHITE

        val header = new Label("SHOP")

        val glad1 = new Button("")
        val glad2 = new Button("")
        val glad3 = new Button("")
        val glad4 = new Button("")
        val glad5 = new Button("")
        glad1.background = java.awt.Color.GRAY.brighter().brighter()
        glad1.preferredSize = new Dimension(65, 80)
        glad1.verticalTextPosition = Alignment.Bottom
        glad1.verticalAlignment = Alignment.Top
        glad1.horizontalTextPosition = Alignment.Center
        glad2.background = java.awt.Color.WHITE
        glad2.preferredSize = new Dimension(65, 80)
        glad2.verticalTextPosition = Alignment.Bottom
        glad2.verticalAlignment = Alignment.Top
        glad2.horizontalTextPosition = Alignment.Center
        glad3.background = java.awt.Color.WHITE
        glad3.preferredSize = new Dimension(65, 80)
        glad3.verticalTextPosition = Alignment.Bottom
        glad3.verticalAlignment = Alignment.Top
        glad3.horizontalTextPosition = Alignment.Center
        glad4.background = java.awt.Color.WHITE
        glad4.preferredSize = new Dimension(65, 80)
        glad4.verticalTextPosition = Alignment.Bottom
        glad4.verticalAlignment = Alignment.Top
        glad4.horizontalTextPosition = Alignment.Center
        glad5.background = java.awt.Color.WHITE
        glad5.preferredSize = new Dimension(65, 80)
        glad5.verticalTextPosition = Alignment.Bottom
        glad5.verticalAlignment = Alignment.Top
        glad5.horizontalTextPosition = Alignment.Center
        val glad1_l = new Label("")
        val glad2_l = new Label("")
        val glad3_l = new Label("")
        val glad4_l = new Label("")
        val glad5_l = new Label("")

        header.font = new Font("Verdana", 1, 20)

        glad1_l.verticalAlignment = Alignment.Top

        add(header, constraints(0, 0, 1, 1, 0, 0.1))

        add(glad1, constraints(0, 1))
        add(glad1_l, constraints(0, 2, 1, 1, 0, 0.1))

        add(glad2, constraints(0, 4))
        add(glad2_l, constraints(0, 5, 1, 1, 0, 0.1))

        add(glad3, constraints(0, 7))
        add(glad3_l, constraints(0, 8, 1, 1, 0, 0.1))

        add(glad4, constraints(0, 10))
        add(glad4_l, constraints(0, 11, 1, 1, 0, 0.1))

        add(glad5, constraints(0, 13))
        add(glad5_l, constraints(0, 14, 1, 1, 0, 0.1))


        listenTo(glad1, glad2, glad3, glad4, glad5)
        val glad_buttons: List[Button] = List(glad1, glad2, glad3, glad4, glad5)
        reactions += {
            case ButtonClicked(b) =>
                if (!controller.players(controller.gameStatus.id).boughtGladiator) {
                    for ((c, i) <- glad_buttons.zipWithIndex) {
                        if (c == b) {
                            controller.selectedGlad = controller.shop.stock(i)._1
                            for (i <- controller.baseArea(controller.players(controller.gameStatus.id))) {
                                cells(i._1)(i._2).setHighlightedSand()
                            }
                            refreshStatus
                        }
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

    val gladPanel = new GridPanel(1, 3) {
        val gladType = new TextField("TY:\nTest")	
        gladType.font = new Font("Verdana", 1, 20)	
        gladType.horizontalAlignment = Alignment.Center

        val gladAP = new Button("")
        gladAP.font = new Font("Algeria", 1, 20)
        gladAP.foreground = java.awt.Color.BLACK
        gladAP.horizontalAlignment = Alignment.Center
        gladAP.background = java.awt.Color.WHITE

        val gladHP = new Button(" ")
        gladHP.font = new Font("Verdana", 1, 20)
        gladHP.foreground = java.awt.Color.BLACK
        gladHP.horizontalAlignment = Alignment.Center
        gladHP.background = java.awt.Color.WHITE

        contents += gladHP
        contents += gladAP
    }
    var statusPanel = new GridPanel(1, 4) {
        var statusline = new TextField(controller.gameStatus.toString, 1)
        statusline.font = new Font("Verdana", 1, 30)
        statusline.horizontalAlignment = Alignment.Center
        statusline.editable = false
        statusline.background = java.awt.Color.WHITE

        var credits = new TextField(controller.players(controller.gameStatus.id).credits.toString + " $", 1)
        credits.font = new Font("Dialog", 1, 25)
        credits.foreground = java.awt.Color.GREEN.darker().darker()
        credits.editable = false
        credits.horizontalAlignment = Alignment.Center
        credits.background = java.awt.Color.WHITE

        var command = new TextField("", 1)
        command.font = new Font("Verdana", 1, 25)
        command.foreground = java.awt.Color.GREEN.darker().darker()
        command.editable = false
        command.horizontalAlignment = Alignment.Center

        var next = new Button("NEXT")
        next.font = new Font("Verdana", 1, 30)
        next.background = java.awt.Color.WHITE
        listenTo(next)
        reactions += {
            case ButtonClicked(b) =>
                if (b == next)
                    controller.endTurn()
        }
        contents += statusline
        contents += credits
        contents += next
    }


    val infoPanel: GridPanel = new GridPanel(1, 1) {
        contents += gladPanel
    }

    contents = new BorderPanel {
        add(shopPanel, BorderPanel.Position.West)
        add(statusPanel, BorderPanel.Position.North)
        add(gridPanel, BorderPanel.Position.Center)
        add(infoPanel, BorderPanel.Position.South)
    }

    def gridPanel: GridPanel = new GridPanel(controller.playingField.size, controller.playingField.size) {
        background = java.awt.Color.BLACK
        for {
            line <- 0 until controller.playingField.size
            row <- 0 until controller.playingField.size
        } {
            val cellPanel = new CellPanel(line, row, controller, 55, 55)
            cells(line)(row) = cellPanel
            listenTo(cellPanel)
            contents += cellPanel
        }
    }

    redraw()
    visible = true

    menuBar = new MenuBar {
        contents += new Menu("Menu") {
            contents += new MenuItem(scala.swing.Action("New Game") {
                controller.resetGame()
                redraw()
            })
            contents += new Separator()
            contents += new MenuItem(scala.swing.Action("New Map") {
                controller.createRandom(controller.playingField.size)
            })
            contents += new Separator()
            contents += new MenuItem(scala.swing.Action("Save Map") {
                controller.save
            })
            contents += new MenuItem(scala.swing.Action("Load Map") {
                controller.load
            })
            contents += new Separator()
            contents += new MenuItem(scala.swing.Action("Playernames") {
                var nameInput = JOptionPane.showInputDialog(
                    null,
                    "Player One",
                    "Change Names",
                    JOptionPane.QUESTION_MESSAGE
                )
                controller.setPlayerName(0, nameInput)
                nameInput = JOptionPane.showInputDialog(
                    null,
                    "Player Two",
                    "Change Names",
                    JOptionPane.QUESTION_MESSAGE
                )
                controller.players(1) = controller.players(0).updateName(nameInput)
                refreshStatus
            })
            contents += new Separator()
            contents += new MenuItem(scala.swing.Action("Exit") {
                System.exit(0)
            })

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
        case event: GameStatusChanged => refreshStatus
        case event: GameOver =>
            val string = controller.players(controller.gameStatus.id)
            JOptionPane.showMessageDialog(
                null,
                controller.players(controller.gameStatus.id) + " won",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
            )
            redraw()
        case event: CellClicked =>
            redraw()
            controller.getGladiatorOption(Coordinate(controller.selectedCell._1, controller.selectedCell._2)) match {
                case None =>
                case Some(g) => {
                    if (!g.moved && g.player == controller.players(controller.gameStatus.id)) {
                        for {
                            i <- 0 until controller.playingField.size
                            j <- 0 until controller.playingField.size
                        } {
                            if (controller.checkMovementPointsAttack(g, g.line, g.row, i, j)) {
                                if //(cells(i)(j).myCell.cellType != CellType.PALM &&
                                (!(i == g.line && j == g.row)) {
                                    cells(i)(j).cell.border = LineBorder(java.awt.Color.RED.darker(), 4)
                                }
                            }

                        }
                        val validMoveCoordinates = controller.playingField.getValidMoveCoordinates(g, Coordinate(g.line, g.row))
                        validMoveCoordinates.foreach(coord => {
                            cells(coord.line)(coord.row).setHighlightedSand()
                        })
                    }
                }
            }
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
        controller.getGladiatorOption(Coordinate(controller.selectedCell._1, controller.selectedCell._2)) match {
            case Some(g) =>
                gladPanel.gladType.text = g.gladiatorType.toString
                gladPanel.gladAP.text = g.ap.toString
                gladPanel.gladHP.text = g.hp.toString
                gladPanel.gladHP.icon = resizedTexture("textures/hp_black.png", 40, 40)
                gladPanel.gladAP.icon = resizedTexture("textures/ap_black.png", 40, 40)
            case None =>
                gladPanel.gladType.text = ""
                gladPanel.gladAP.text = " "
                gladPanel.gladHP.text = " "
                gladPanel.gladAP.icon = resizedTexture("textures/empty_small.png", 40, 40)
                gladPanel.gladHP.icon = resizedTexture("textures/empty_small.png", 40, 40)
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
        for ((g, i) <- controller.shop.stock.zipWithIndex) {
            i match {
                case 0 => shopPanel.glad1.icon = getGladIcon(g._1)
                    shopPanel.glad1.text = controller.shop.calcCost(g._1) + "$"
                    shopPanel.glad1_l.text = "<html><body>HP: " + g._1.hp.toInt + "<br>AP: " + g._1.ap.toInt + "<br>MV: " + g._1.movementPoints.toInt + "</body></html>\""
                case 1 => shopPanel.glad2.icon = getGladIcon(g._1)
                    shopPanel.glad2.text = controller.shop.calcCost(g._1) + "$"
                    shopPanel.glad2_l.text = "<html><body>HP: " + g._1.hp.toInt + "<br>AP: " + g._1.ap.toInt + "<br>MV: " + g._1.movementPoints.toInt + "</body></html>\""
                case 2 => shopPanel.glad3.icon = getGladIcon(g._1)
                    shopPanel.glad3.text = controller.shop.calcCost(g._1) + "$"
                    shopPanel.glad3_l.text = "<html><body>HP: " + g._1.hp.toInt + "<br>AP: " + g._1.ap.toInt + "<br>MV: " + g._1.movementPoints.toInt + "</body></html>\""
                case 3 => shopPanel.glad4.icon = getGladIcon(g._1)
                    shopPanel.glad4.text = controller.shop.calcCost(g._1) + "$"
                    shopPanel.glad4_l.text = "<html><body>HP: " + g._1.hp.toInt + "<br>AP: " + g._1.ap.toInt + "<br>MV: " + g._1.movementPoints.toInt + "</body></html>\""
                case 4 => shopPanel.glad5.icon = getGladIcon(g._1)
                    shopPanel.glad5.text = controller.shop.calcCost(g._1) + "$"
                    shopPanel.glad5_l.text = "<html><body>HP: " + g._1.hp.toInt + "<br>AP: " + g._1.ap.toInt + "<br>MV: " + g._1.movementPoints.toInt + "</body></html>\""
                case _ =>
            }
        }
    }

    def getGladIcon(glad: Gladiator): ImageIcon = {
        glad.gladiatorType match {
            case GladiatorType.SWORD
            => resizedTexture("textures/sword.png", 50, 50)
            case GladiatorType.BOW
            => resizedTexture("textures/bow.png", 50, 50)
            case GladiatorType.TANK
            => resizedTexture("textures/shield_small.png", 50, 50)
        }
    }

    def changePlayerNames(): Unit = {
        val textArea = new JTextArea()
        textArea.setEditable(true)
        val scrollPane = new JScrollPane(textArea)
        scrollPane.requestFocus()
        textArea.requestFocusInWindow()
        scrollPane.setPreferredSize(new Dimension(800, 600))
        JOptionPane.showMessageDialog(textArea, "jo")
        val info = textArea.getText()
    }

    def resizedTexture(path: String, width: Int, height: Int): ImageIcon = {
        var imageIcon = new ImageIcon(path)
        var image = imageIcon.getImage; // transform it
        var newimg = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg); // transform it back
        imageIcon
    }
}
