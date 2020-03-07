package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

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
	
	//Feature 11, moveCurrentDomino
	public static void moveCurrentDomino(DominoInKingdom currentDomino, DirectionKind newDirection) {
		
		Kingdom currentKD = currentDomino.getKingdom();
		DominoStatus newCurrentDominoStatus;
		int currentDominoX = currentDomino.getX();
		int currentDominoY = currentDomino.getY();
		boolean placementError = false;
		
		
		if(newDirection == DirectionKind.Up) {
			currentDomino.setDirection(DirectionKind.Up);
			currentDomino.setY(currentDominoY + 1);
			currentDominoY ++;
		}else if (newDirection == DirectionKind.Down) {
			currentDomino.setDirection(DirectionKind.Down);
			currentDomino.setY(currentDominoY - 1);
			currentDominoY --;
		}else if (newDirection == DirectionKind.Left) {
			currentDomino.setDirection(DirectionKind.Left);
			currentDomino.setX(currentDominoX - 1);
			currentDominoX --;
		}else {
			currentDomino.setDirection(DirectionKind.Right);
			currentDomino.setX(currentDominoX + 1);
			currentDominoX ++;
		}
		
		
		
		if(currentDominoX == 0 && currentDominoY == 0) {
			placementError = true;
		}else if(currentDominoX < -9 || currentDominoX > 9) {
			placementError = true;
		}else if(currentDominoY < -9 || currentDominoY > 9) {
			placementError = true;
		}else {
			placementError = false;
		}
		
		for(int i = 0; i < currentKD.getTerritories().size(); i++) {
			if(currentDomino.getX()==currentKD.getTerritory(i).getX() || currentDomino.getY()==currentKD.getTerritory(i).getY()){
				placementError = true;
			}
		}
		
		
		
		
		
		if(placementError = true) {
			currentDomino.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		}else {
			currentDomino.getDomino().setStatus(DominoStatus.CorrectlyPreplaced);
		}
		
		
		
		
	}
	
	//Feature 12, rotateCurrentDomino
	public static void rotateCurrentDomino(Boolean isClockwise,DominoInKingdom currentDomino) {
		
		Kingdom currentKD = currentDomino.getKingdom();
		
		if(isClockwise == true) {
			if(currentDomino.getDirection()== DirectionKind.Up) {
				
				currentDomino.setDirection(DirectionKind.Right);
				currentDomino.setX(currentDomino.getX() + 1);
				currentDomino.setY(currentDomino.getY() - 1);
			
			}else if(currentDomino.getDirection()== DirectionKind.Down) {
				
				currentDomino.setDirection(DirectionKind.Left);
				currentDomino.setX(currentDomino.getX() - 1);
				currentDomino.setY(currentDomino.getY() + 1);
			
			}else if(currentDomino.getDirection()== DirectionKind.Left) {
				
				currentDomino.setDirection(DirectionKind.Up);
				currentDomino.setX(currentDomino.getX() + 1);
				currentDomino.setY(currentDomino.getY() + 1);
			
			}else {
				
				currentDomino.setDirection(DirectionKind.Down);
				currentDomino.setX(currentDomino.getX() - 1);
				currentDomino.setY(currentDomino.getY() - 1);
			}
		
		}else {
			if(currentDomino.getDirection()== DirectionKind.Up) {
				
				currentDomino.setDirection(DirectionKind.Left);
				currentDomino.setX(currentDomino.getX() - 1);
				currentDomino.setY(currentDomino.getY() - 1);
				
			}else if(currentDomino.getDirection()== DirectionKind.Down) {
				
				currentDomino.setDirection(DirectionKind.Right);
				currentDomino.setX(currentDomino.getX() + 1);
				currentDomino.setY(currentDomino.getY() + 1);
				
			}else if(currentDomino.getDirection()== DirectionKind.Left) {
				
				currentDomino.setDirection(DirectionKind.Down);
				currentDomino.setX(currentDomino.getX() + 1);
				currentDomino.setY(currentDomino.getY() - 1);
				
			}else {
				
				currentDomino.setDirection(DirectionKind.Up);
				currentDomino.setX(currentDomino.getX() - 1);
				currentDomino.setY(currentDomino.getY() + 1);
				
			}
			
			
		}
		
		int currentDominoX = currentDomino.getX();
		int currentDominoY = currentDomino.getY();
		boolean placementError = false;
		
		//myCourses
		if(currentDominoX > 4 || currentDominoX < -4 || currentDominoY > 4 || currentDominoY < -4)  {
			placementError = true;
		}else if(currentDominoX == 0 && currentDominoY == 0) {
			placementError = true;
		}else if(currentDominoX < -9 || currentDominoX > 9) {
			placementError = true;
		}else if(currentDominoY < -9 || currentDominoY > 9) {
			placementError = true;
		}else {
			placementError = false;
		}
		
		for(int i = 0; i < currentKD.getTerritories().size(); i++) {
			if(currentDomino.getX()==currentKD.getTerritory(i).getX() || currentDomino.getY()==currentKD.getTerritory(i).getY()){
				placementError = true;
			}
		}
		
		
		if(placementError = true) {
			currentDomino.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		}else {
			currentDomino.getDomino().setStatus(DominoStatus.CorrectlyPreplaced);
		}
		
	}
	
	
	//Feature 13, placeCurrentDomino
	public static void placeCurrentDomino(DominoInKingdom currentDomino) {
		
		Kingdom currentKD = currentDomino.getKingdom();
		
		if(currentDomino.getDomino().getStatus() == DominoStatus.CorrectlyPreplaced) {
			currentDomino.getDomino().setStatus(DominoStatus.PlacedInKingdom);
			currentKD.addTerritory(currentDomino); //Don't know about this tbh
			currentDomino = new DominoInKingdom(currentDomino.getX(), currentDomino.getY(), currentDomino.getKingdom(), currentDomino.getDomino());
		
		}else {
			throw new RuntimeException("Can't place a domino that isn't correctly preplaced!"); 
		}
	}
	
	//Feature 18, discardDomino
	public static void discardDomino(DominoInKingdom currentDomino) {
		
		if(currentDomino.getDomino().getStatus()==DominoStatus.ErroneouslyPreplaced) {
			
		}else {
			throw new RuntimeException("Can't discard a domino that is correctly preplaced!");
		}
	}
	
	
	
	
	
	
	
}
