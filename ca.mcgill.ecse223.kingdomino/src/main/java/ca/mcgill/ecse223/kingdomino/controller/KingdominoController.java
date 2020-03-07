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
	
	/**
	 * SetGameOptions method that set the number of players of the game with param num
	 * @param num
	 * @param kingdomino
	 * @throws Exception
	 * @author Abdallah Shapsough
	 */
	public static void SetGameOptions(int num, Kingdomino kingdomino) throws Exception {
		if(num<2||num>4) {
			throw new Exception("Number of players has to be between 2 and 4");
		}
		
		Game game = kingdomino.getCurrentGame();
		game.setNumberOfPlayers(num);
	}
	

	/**
	 * SetGameOptions method that takes in different parameters and is responsible for adding or removing the 
	 * selected bonus options from the current game
	 * string: must be equal to either "is" or "is not"
	 * bonus: must be equal to either "isUsingHarmony" or "isUsingMiddleKingdom"
	 * kingdomino: must be equal to Kingdomino kingdomino
	 * @param string
	 * @param kingdomino
	 * @param bonus
	 * @author Abdallah Shapsough
	 */
	public static void SetGameOptions(String string, Kingdomino kingdomino, String bonus) {
		
		//get current game of kingdomino
		Game game = kingdomino.getCurrentGame();
		
		//either add selected bonus option if string = "is" or find and remove selected bonus option if string = "is not"
			BonusOption bonusOption= new BonusOption(bonus, kingdomino);
			if(string.equals("is")) {
				game.addSelectedBonusOption(bonusOption);
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
	
	public static void ProvideUserProfile(String userName, int number, String string) {
		if(string.equals("playedGames")) {
			User.getWithName(userName).setPlayedGames(number);
		}else if(string.equals("wonGames")) {
			User.getWithName(userName).setWonGames(number);
		}
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
