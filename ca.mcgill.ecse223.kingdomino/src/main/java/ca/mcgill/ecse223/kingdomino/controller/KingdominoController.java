package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import java.util.*;


public class KingdominoController {
	
	public KingdominoController() {
		
	}
	
	public static void main(String args[]) {
		
	}
	
	public static void ProvideUserProfile(String userName, Kingdomino kingdomino) throws Exception {
	
		if(userName == null) {
		throw new Exception("username cannot be null");
		}
		if(userName.isEmpty()) {
			throw new Exception("username cannot be empty");
		}
		if(userName.trim().length()==0) {
			throw new Exception("username must have characters");
		}
		int length = userName.length();
	      for (int i = 0; i < length; i++) {
	         // checks whether the character is neither a letter nor a digit
	         // if it is neither a letter nor a digit then it will return false
	         if ((Character.isLetterOrDigit(userName.charAt(i)) == false)) {
	            throw new Exception("username can only contain numbers and letters");
	         }
	      }

	      
		kingdomino.addUser(userName.trim().toLowerCase());

	}
	
	
	public static ArrayList<User> ProvideUserProfile(Kingdomino kingdomino) {
		ArrayList<User> users = new ArrayList<User>(kingdomino.getUsers());
		Collections.sort(users, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
		return users;
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
