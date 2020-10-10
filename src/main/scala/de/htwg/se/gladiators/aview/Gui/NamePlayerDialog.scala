package de.htwg.se.gladiators.aview.Gui

import scala.swing.Dialog.Message
import javax.swing.JOptionPane
import scala.swing.Swing.EmptyIcon

object NamePlayerDialog {
    def namePlayer(name: String) = {
        val r = JOptionPane.showInputDialog(null, f"What is the name of $name", "Name Player",
            Message.Question.id, EmptyIcon,
            null, "")
        if (r eq null) None else Some(r.asInstanceOf[String])
    }
}
