package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import java.util.*;

public class KingdominoController {
	
	public KingdominoController() {
		
	}
	
	// Calculating the ranking of the players in the game
	public static void calculateRanking(Kingdomino kingdomino) {
		List <Player> players = kingdomino.getCurrentGame().getPlayers();
		Player currentPlayer;
		Player tempPlayer;
		for(int i = 0; i<players.size()-1; i++) {
			currentPlayer = players.get(i);
			for (int j = i+1; j<players.size(); j++) {
				tempPlayer = players.get(j);
				if(tempPlayer.getTotalScore()>currentPlayer.getTotalScore()) {
					players.set(i, tempPlayer);
					players.set(j, currentPlayer);
					currentPlayer = tempPlayer;
				} else if (tempPlayer.getTotalScore()==currentPlayer.getTotalScore()) {
					if(getLargestPropertySize(tempPlayer.getKingdom().getProperties()) > getLargestPropertySize(currentPlayer.getKingdom().getProperties())) {
						players.set(i, tempPlayer);
						players.set(j, currentPlayer);
						currentPlayer = tempPlayer;
					} else if (getNumberCrowns(tempPlayer.getKingdom().getProperties()) > getNumberCrowns(currentPlayer.getKingdom().getProperties())) {
						players.set(i, tempPlayer);
						players.set(j, currentPlayer);
						currentPlayer = tempPlayer;
					}
				}
			}
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setCurrentRanking(i+1);
		}
	}
	
	private static int getLargestPropertySize(List<Property> properties) {
		if(properties == null) {
			return 0;
		}
		Property largestProperty = properties.get(0);
		if(properties.size()==1) {
			return largestProperty.getSize();
		}
		for(int i = 1; i<properties.size(); i++) {
			if(properties.get(i).getSize()>largestProperty.getSize()) {
				largestProperty = properties.get(i);
			}
		}
		return largestProperty.getSize();
		
	}
	private static int getNumberCrowns(List<Property> properties) {
		int numCrowns=0;
		for(Property property : properties) {
			numCrowns+=property.getCrowns();
		}
		return numCrowns;
	}
	
}
