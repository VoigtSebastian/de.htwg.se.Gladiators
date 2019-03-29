case class Gladiator(x:Int, y:Int, movementPoints:Double, strenghth:Double, health:Double);

val gladiator = Gladiator(0,0, 2.0, 5.0, 1.0);

gladiator.x;
gladiator.y;
gladiator.movementPoints;
gladiator.strenghth;
gladiator.health;

case class Cell(x:Int, y:Int, movementCost:Double, cellType:Int);
case class Grid(cell:Array[Cell]);

val cell0 = Cell(5,0,1.0,0);
val cell1 = Cell(0,0,1.0,0);
val cell2 = Cell(0,0,1.0,0);

val array = new Array[Cell](3);
array(0) = cell0;
array(1) = cell0;
array(2) = cell0;

val grid = Grid(array);

grid.cell(0).x;