package ca.mcgill.ecse223.kingdomino;

import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;

public class KingdominoApplication {

	private static Kingdomino kingdomino;
	private static Gameplay gameplay;

	public static void main(String[] args) {
		System.out.println("Hello World!");
	}

	public static Kingdomino getKingdomino() {
		if (kingdomino == null) {
			kingdomino = new Kingdomino();
		}
		return kingdomino;
	}

	public static void setKingdomino(Kingdomino kd) {
		kingdomino = kd;
	}
	
	public static Gameplay getGameplay() {
		return gameplay;
	}
	
	public static void setGameplay(Gameplay gp) {
		gameplay = gp;
	}
}
