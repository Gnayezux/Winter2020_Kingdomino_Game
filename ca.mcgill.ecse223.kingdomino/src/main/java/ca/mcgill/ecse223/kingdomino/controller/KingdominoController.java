package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import java.util.*;

public class KingdominoController {
	
	public KingdominoController() {
		
	}
	public static void main(String[] args) throws Exception {

		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		game.setNumberOfPlayers(4);
		kingdomino.setCurrentGame(game);

		SetGameOptions(3, kingdomino);
	

		SetGameOptions("is not", kingdomino, "isUsingHarmony");
		
		
		
		System.out.print(game.getSelectedBonusOptions());

	}
	

	//set bonus options that are available
	public static void SetGameOptions(Kingdomino kingdomino) {
	
	}
	
	
	public static void SetGameOptions(int num, Kingdomino kingdomino) throws Exception {
		if(num<2||num>4) {
			
			throw new Exception("Number of players has to be between 2 and 4");
		}
		
		Game game = kingdomino.getCurrentGame();
		game.setNumberOfPlayers(num);
	}
	

	
	public static void SetGameOptions(String string, Kingdomino kingdomino, String bonus) {
		Game game = kingdomino.getCurrentGame();
		if(bonus.equals("isUsingHarmony")) {
			BonusOption isUsingHarmony= new BonusOption(bonus, kingdomino);
			if(string.equals("is")) {
				game.addSelectedBonusOption(isUsingHarmony);
			}else {
				BonusOption toRemove = null;
				for(BonusOption temp : game.getSelectedBonusOptions()) {
					if(bonus.equals(temp.getOptionName())) {
						toRemove = temp;
					}
				}
				game.removeSelectedBonusOption(toRemove);
			}
		} else if(bonus.equals("isUsingMiddleKingdom")) {
			BonusOption isUsingMiddleKingdom= new BonusOption(bonus, kingdomino);
			if(string.equals("is")) {
				game.addSelectedBonusOption(isUsingMiddleKingdom);
			}else {
				BonusOption toRemove = null;
				for(BonusOption temp : game.getSelectedBonusOptions()) {
					if(bonus.equals(temp.getOptionName())) {
						toRemove = temp;
					}
				}
				game.removeSelectedBonusOption(toRemove);
			}
		}
	}
	

	
	// Calculating the ranking of the players in the game
	public static void calculateRanking(Kingdomino kingdomino) {
		//Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		List <Player> players = kingdomino.getCurrentGame().getPlayers();

		for (int i = 1; i < players.size(); i++) {
			Player currentPlayer = players.get(i);
			int j = i - 1;
			while (j>=0 && currentPlayer.getTotalScore() < players.get(j).getTotalScore()) {
				players.set(j+1, players.get(j));
				j--;
			}
			players.set(j+1, currentPlayer);
			players.get(i-1).setCurrentRanking(i);
		}
	}
	
}
