package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import java.util.*;

public class KingdominoController {
	
	public KingdominoController() {
		
	}
	public static void main(String[] args) {
		SetGameOptions(4,true,true);
//		System.out.print(createUser("Shap"));
		//hello world
	}
	
//	public static boolean createUser(String name) {
//		Kingdomino currKingdomino = KingdominoApplication.getKingdomino();
//		//isletterordigit??
//		User newUser = new User(name,currKingdomino);
//		User dd = new User("ha",currKingdomino);
//		for(User user : currKingdomino.getUsers()) {
//			System.out.print(user.getName());
//		}
//		
//		//adduserat
//		return true;
//
//	}
	
//	public static boolean initializeGame() {
//		Kingdomino currKingdomino = KingdominoApplication.getKingdomino();
//		Game newGame = new Game(48, currKingdomino);
//		currKingdomino.setCurrentGame(newGame);
//		return true;
//	}
	
	public static Boolean SetGameOptions(int players,boolean harmony, boolean middlekingdom) {
		Kingdomino currKingdomino = KingdominoApplication.getKingdomino();
		int numPlayers = players;
		if(harmony==true) {
			BonusOption isUsingHarmony= new BonusOption("Harmony", currKingdomino);
			currKingdomino.addBonusOption(isUsingHarmony);
		}
		if(middlekingdom==true) {
			BonusOption isUsingMiddleKingdom= new BonusOption("Middle Kingdom", currKingdomino);
			currKingdomino.addBonusOption(isUsingMiddleKingdom);
		}
		
		int pileSize = 48;
		
		if(numPlayers==3) {
			pileSize = 36;
		}else if(numPlayers==2) {
			pileSize =24;
		}
		
		Game newGame = new Game(pileSize, currKingdomino);
		
		newGame.setNumberOfPlayers(numPlayers);
		
		for(BonusOption option : currKingdomino.getBonusOptions()) {
			newGame.addSelectedBonusOption(option);
		}
		
		currKingdomino.setCurrentGame(newGame);
		
		return true;
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
