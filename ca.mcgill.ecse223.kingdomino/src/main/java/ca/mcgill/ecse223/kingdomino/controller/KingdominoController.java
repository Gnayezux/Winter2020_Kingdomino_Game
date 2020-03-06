package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;

import java.util.*;

public class KingdominoController {
	
	public KingdominoController() {
		
	}
	public boolean VerifyNoOverlapping (Domino aDomino,Kingdom aKingdom, int x, int y, DirectionKind aDirection) {
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
		for (KingdomTerritory d: aKingdom.getTerritories()) {
			coord temp = new coord(d.getX(),d.getY());
			if (d.getClass().toString() == "DominoInKingdom") {
				DominoInKingdom dik = (DominoInKingdom) d;
				int x2 = 0,y2 = 0;
				switch (dik.getDirection()) {
				case Up:
					y2 = dik.getY()+1;
					x2 = dik.getX();
				case Left:
					x2 = dik.getX()-1;
					y2 = dik.getY();
				case Right:
					x2 = dik.getX()+1;
					y2 = dik.getY();
				case Down:
					x2 = dik.getX();
					y2 = dik.getY()-1;
					}
				coord leftcoord, rightcoord;
				leftcoord = new coord(dik.getX(),dik.getY());
				rightcoord = new coord(x2,y2);
				if (leftcoord.equalsTo(tileOne) || leftcoord.equals(tileTwo) || rightcoord.equalsTo(tileOne) || rightcoord.equalsTo(tileTwo)) {
					return false;
				}
			}
		}
		return true;
	}
}
