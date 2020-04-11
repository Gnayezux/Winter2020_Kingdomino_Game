package ca.mcgill.ecse223.kingdomino;

import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;

import java.io.File;

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;

public class KingdominoApplication {

	private static Kingdomino kingdomino;

	public static void main(String[] args) {

		Kingdomino wtf = getKingdomino();

		KingdominoController.load(wtf, "D:" + File.separator+"kingdomino_test_game_2.mov"); //Change this directory to the directory to load game file
		KingdominoController.identifyProperties(wtf);
		wtf.getCurrentGame().setNextPlayer(wtf.getCurrentGame().getPlayer(2));
		KingdominoController.identifyProperties(wtf);
		wtf.getCurrentGame().setNextPlayer(wtf.getCurrentGame().getPlayer(1));
		KingdominoController.identifyProperties(wtf);
		wtf.getCurrentGame().setNextPlayer(wtf.getCurrentGame().getPlayer(0));
		KingdominoController.identifyProperties(wtf);
		KingdominoController.calculatePropertyAttributes(wtf);
		KingdominoController.calculatePropertyAttributes(wtf);
		KingdominoController.calculatePropertyAttributes(wtf);
		KingdominoController.calculatePropertyAttributes(wtf);
		KingdominoController.calculatePlayerScore(wtf);
		KingdominoController.save(wtf, "D:" + File.separator+"WTF.mov");//Change this directory to the directory to save game file
		System.out.println("Game file successfully created.");
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
}
