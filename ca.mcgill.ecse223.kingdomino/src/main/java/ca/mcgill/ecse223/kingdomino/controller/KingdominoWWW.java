package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController.coord;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.KingdomTerritory;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
class coord {
	public int x;
	public int y;
	public coord(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public boolean equalsTo(coord aCoord) {
		return (this.x == aCoord.x && this.y == aCoord.y);
	}
}
public class KingdominoWWW {
	public boolean VerifyCastleAdjacency (int x, int y, DirectionKind aDirection, Castle aCastle) {
		int x1 = 0, y1 = 0;
		switch (aDirection) {
		case Up:
			y1 = y+1;
			x1 = x;
		case Left:
			x1 = x-1;
			y1 = y;
		case Right:
			x1 = x+1;
			y1 = y;
		case Down:
			x1 = x;
			y1 = y-1;
			
		}
		coord tileOne = new coord(x,y);
		coord tileTwo = new coord(x1,y1);
		coord origin = new coord(0,0);
		coord up = new coord(0,1);
		coord right = new coord(1,0);
		coord left = new coord(-1,0);
		coord down = new coord(0,-1);
		if ((tileOne.equalsTo(up) ||tileOne.equalsTo(right)||tileOne.equalsTo(left)||tileOne.equalsTo(down)) && !tileTwo.equalsTo(origin)) {
			return true;
		}
		if ((tileTwo.equalsTo(up) ||tileTwo.equalsTo(right)||tileTwo.equalsTo(left)||tileTwo.equalsTo(down)) && !tileOne.equalsTo(origin)) {
			return true;
		}
		return false;
	

	}
}
