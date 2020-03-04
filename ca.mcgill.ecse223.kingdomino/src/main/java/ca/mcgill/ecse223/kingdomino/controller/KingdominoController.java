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
			aProperty.setKingdom(aKingdom);
			myProperties[i] = aProperty;
			}
		}
		return myProperties;
		
	}
	
	public void CalculatePropertyAttributes(Property aProperty){
		int numCrowns=0;
		int size =0;
		for (int i=0; i< aProperty.numberOfIncludedDominos(); i++) {
			if(aProperty.getLeftTile()== aProperty.getIncludedDomino(i).getLeftTile()) {
				numCrowns += aProperty.getIncludedDomino(i).getLeftCrown();
			}
			else if (aProperty.getLeftTile()== aProperty.getIncludedDomino(i).getRightTile()){
				numCrowns += aProperty.getIncludedDomino(i).getRightCrown();
			}
			
			size++;
		}
		aProperty.setCrowns(numCrowns);
		aProperty.setSize(size);
	}
	
	public int CalculateBonusScore(DominoInKingdom[] playedDominoes, Kingdom aKingdom) {
		int bonus = 0;
		if (playedDominoes.length ==12) {
			bonus +=5;
		}
		
			
		return bonus;
		
	}
	
	public int CalculatePlayerScore(DominoInKingdom[] playedDominoes, Kingdom aKingdom) {
		int score =0;
		int pscore =0;
		int bonuscore =0;
		Property[] myProp = IdentifyKingdomProperties(playedDominoes,aKingdom);
		for(int i=0; i< myProp.length; i++) {
			if (myProp[i]!=null) {
				pscore = myProp[i].getCrowns()*myProp[i].getSize();
				score+=pscore;
			}
			
		}
		bonuscore = CalculateBonusScore(playedDominoes, aKingdom);
		score += bonuscore;
		return score;
	}
	
	
}
