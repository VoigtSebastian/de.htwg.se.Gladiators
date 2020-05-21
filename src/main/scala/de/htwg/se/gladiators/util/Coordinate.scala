package de.htwg.se.gladiators.util

case class Coordinate(val line: Int, val row: Int) {
    override def equals(that: Any): Boolean =
        that match {
        case that: Coordinate => that.canEqual(this) && this.line == that.line && this.row == that.row
        case _ => false
    }
}
