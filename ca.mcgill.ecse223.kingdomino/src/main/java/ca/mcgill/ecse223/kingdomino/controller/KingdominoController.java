package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import java.util.*;

public class KingdominoController {
	
	public KingdominoController() {
		
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
	
	public Property[] IdentifyKingdomProperties(DominoInKingdom[] playedDominoes, Kingdom aKingdom){
		
		
		Property[] myProperties= new Property[playedDominoes.length];
		for(int i=0; i<playedDominoes.length; i++) {
			Property aProperty = new Property(aKingdom);
			for(int j=i+1; j<playedDominoes.length; j++) {
				if(playedDominoes[i].getX()==playedDominoes[j].getX()+1) {
					
				}
			
			aProperty.setLeftTile(playedDominoes[i].getDomino().getLeftTile());
			aProperty.addIncludedDomino(playedDominoes[i].getDomino());
			myProperties[i] = aProperty;
			}
		}
		return myProperties;
		
	}
	
}
