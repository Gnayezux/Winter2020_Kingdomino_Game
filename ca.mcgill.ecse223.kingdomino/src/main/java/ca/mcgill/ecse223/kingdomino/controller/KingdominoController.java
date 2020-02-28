package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KingdominoController {
	
	public KingdominoController() {
		
	}

	
	public static void ChooseNextDomino(Player curPlayer, Kingdomino kingdomino, String chosen) {
		Game game = kingdomino.getCurrentGame();
		Draft draft = game.getCurrentDraft();
		
		for(int i =0;i<draft.getIdSortedDominos().size();i++) {
			if(Integer.parseInt(chosen)==draft.getIdSortedDomino(i).getId()) {
				try {
					draft.addSelection(curPlayer, draft.getIdSortedDomino(i));
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
//		System.out.print(draft.getSelections().get(2).getPlayer());
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
