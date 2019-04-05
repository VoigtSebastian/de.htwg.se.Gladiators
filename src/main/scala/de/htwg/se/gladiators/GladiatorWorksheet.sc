//define basic classes
case class Gladiator(x: Int, y: Int, movementPoints: Double, strength: Double, health: Double)

case class Cell(movementCost: Double, cellType: Int)

case class Grid(cells: Array[Array[Cell]])

//define basic gridTypes translates to how many movementPoints a player has to invest to move onto the cell.
def base = 0
def sand = 1
def marsh = 2
def water = 3

//create basic objects and test stuff
val gladiator1 = Gladiator(0, 0, 3.0, 5.0, 1.0)
gladiator1.x
gladiator1.y
gladiator1.movementPoints
gladiator1.strength
gladiator1.health

val cells = Array.ofDim[Cell](3, 3)
cells(0)(0) = Cell(1.0, sand)
cells(0)(1) = Cell(1.0, base)
cells(0)(2) = Cell(1.0, sand)

cells(1)(0) = Cell(1.0, water)
cells(1)(1) = Cell(1.0, marsh)
cells(1)(2) = Cell(1.0, marsh)

cells(2)(0) = Cell(1.0, sand)
cells(2)(1) = Cell(1.0, base)
cells(2)(2) = Cell(1.0, sand)

val playingField = Grid(cells)


print("Gladiator1 pos: " + gladiator1.x + "," + gladiator1.y)

println("Printing cell types of current playingField")
for (i <- cells.indices) {
    for (j <- cells(i).indices) {
        print(playingField.cells(i)(j).cellType + " ")
    }
    println()
}