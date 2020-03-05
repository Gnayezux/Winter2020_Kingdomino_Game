package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;

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


	public DominoInKingdom rightTile (DominoInKingdom leftTile) {
		DominoInKingdom rightTile = new DominoInKingdom(leftTile.getX(), leftTile.getY(), leftTile.getKingdom(), leftTile.getDomino());
		TerrainType terr = leftTile.getDomino().getRightTile();
		if(leftTile.getDirection()==DirectionKind.Up) {
			rightTile.setY(leftTile.getY()+1);
		}
		else if (leftTile.getDirection() == DirectionKind.Down) {
			rightTile.setY(leftTile.getY()-1);
		}
		else if (leftTile.getDirection() == DirectionKind.Left) {
			rightTile.setX(leftTile.getX()-1);
		}
		else {
			rightTile.setX(leftTile.getX()+1);
		}
		
		return rightTile;
	}
	
	public boolean isConnected(DominoInKingdom d1, DominoInKingdom d2) {
		if (d1.getX()==d2.getX()&&d1.getY()==d2.getY()-1) {
			return true;
		}
		else if (d1.getX()==d2.getX()&&d1.getY()==d2.getY()+1) {
			return true;
		}
		else if (d1.getY()==d2.getY()&&d1.getX()==d2.getX()-1) {
			return true;
		}
		else if (d1.getY()==d2.getY()&&d1.getX()==d2.getX()+1) {
			return true;
		}
		
		return false;	
	}
	
	public Property[] IdentifyKingdomProperties(DominoInKingdom[] playedDominoes, Kingdom aKingdom){
		
		
		Property[] myProperties= new Property[playedDominoes.length];
		for(int i=0; i<playedDominoes.length; i++) {
			Property aProperty = new Property(aKingdom);
			for(int j=0; j<playedDominoes.length; j++) {
				if(isConnected(playedDominoes[i],playedDominoes[j])) {
					if(playedDominoes[i].getDomino().getLeftTile()==playedDominoes[j].getDomino().getLeftTile()) {
						
					}
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
	
	public int CalculateBonusScore(DominoInKingdom[] playedDominoes, Kingdom aKingdom, Castle castle) {
		int bonus = 0;
		boolean middlexl = false;
		boolean middlexr = false;
		boolean middleyt = false;
		boolean middleyb = false;
		if (playedDominoes.length ==12) {
			bonus +=5;
		}
		
		for (int i=0; i<playedDominoes.length; i++) {
			if(playedDominoes[i].getX()==castle.getX()-2) {
				middlexl = true;
				}
			if (playedDominoes[i].getX()==castle.getX()+2) {
				middlexr = true;
			}
			if (playedDominoes[i].getY()==castle.getY()-2) {
				middleyb = true;
			}
			if (playedDominoes[i].getY()==castle.getY()+2) {
				middleyt = true;
			}
			
		}
		if(middlexl&&middlexr&&middleyb&&middleyt) {
			bonus+=10;
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
				CalculatePropertyAttributes(myProp[i]);
				pscore = myProp[i].getCrowns()*myProp[i].getSize();
				score+=pscore;
			}
			
		}
		bonuscore = CalculateBonusScore(playedDominoes, aKingdom);
		score += bonuscore;
		return score;
	}
	
	
}
