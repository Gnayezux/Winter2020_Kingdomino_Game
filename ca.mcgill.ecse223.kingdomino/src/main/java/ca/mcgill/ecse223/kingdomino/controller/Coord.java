package ca.mcgill.ecse223.kingdomino.controller;

/**
 * This class is a helper class for all the features that perform verifications
 * @author Zeyang Xu
 */
public class Coord {
	public int x;
	public int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	public boolean equalsTo(Coord aCoord) {
		return (this.x == aCoord.getX() && this.y == aCoord.getY());
	}
	
	public boolean adJacentTo(Coord aCoord) {
		return ((this.x == aCoord.getX() && this.y + 1 == aCoord.getY()) || (this.x == aCoord.getX() && this.y - 1 == aCoord.getY())
				|| (this.x + 1 == aCoord.getX() && this.y == aCoord.getY())
				|| (this.x - 1 == aCoord.getX() && this.y == aCoord.getY()));
	}
}
