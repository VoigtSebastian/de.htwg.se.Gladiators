package de.htwg.se.gladiators.util

case class Coordinate(
    x: Int,
    y: Int) {

    def isLegal(dimensions: Int) = (x >= 0 && y >= 0 && y < dimensions && x < dimensions)

    override def equals(that: Any): Boolean =
        that match {
            case that: Coordinate => that.canEqual(this) && this.x == that.x && this.y == that.y
            case _ => false
        }

    override def toString = f"($x, $y)"
}
