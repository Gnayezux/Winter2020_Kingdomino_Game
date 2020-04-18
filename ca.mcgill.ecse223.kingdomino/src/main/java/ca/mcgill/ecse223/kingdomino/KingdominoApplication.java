package ca.mcgill.ecse223.kingdomino;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.view.KingdominoBoardGame;

public class KingdominoApplication {

	private static Kingdomino kingdomino;
	private static Gameplay gameplay;

	public static void main(String[] args) {
		new KingdominoController();
		//KingdominoBoardGame boardGame = new KingdominoBoardGame();
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
