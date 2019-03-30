case class Gladiator(x:Int, y:Int, movementPoints:Double, strength:Double, health:Double);

val gladiator1 = Gladiator(0,0, 2.0, 5.0, 1.0);

gladiator1.x;
gladiator1.y;
gladiator1.movementPoints;
gladiator1.strength;
gladiator1.health;

val gladiator2 = Gladiator(0,1, 4.0, 2.0, 1.0);

case class Cell(x:Int, y:Int, movementCost:Double, cellType:Int);
case class Grid(cell:Array[Cell]);

val cell0 = Cell(0,0,1.0,0);
val cell1 = Cell(0,1,1.0,0);
val cell2 = Cell(1,0,1.0,0);
val cell3 = Cell(1,1,1.0,0);

val array = new Array[Cell](4);
array(0) = cell0;
array(1) = cell1;
array(2) = cell2;
array(3) = cell3;

val grid = Grid(array);

grid.cell(0).x;
grid.cell(1).x;

array.foreach {println}

print("Gladiator1 pos: " + gladiator1.x + "," + gladiator1.y);

for(c <- array) {
  if(c.x == gladiator1.x && c.y == gladiator1.y) {
    print(c + " ist besetzt von Gladiator1");
  }
}