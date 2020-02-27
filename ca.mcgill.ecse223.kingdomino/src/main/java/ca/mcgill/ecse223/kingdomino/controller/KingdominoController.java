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
	
	//****BEGIN FEATURE 3***
	//Starting a New Game
	//As a Kingdomino player, I want to start a new game of Kingdomino against some opponents
	//with my castle placed on my territory with the current settings of the game. 
	//The initial order of player should be randomly determined.

	public static void startNewGame(Kingdomino kingdomino) {
		//should kingdomino be an input or initialized in method
		
		Boolean kingdominoIsValid = !kingdomino.equals(null);

		if (!kingdominoIsValid) {
			throw new IllegalArgumentException("This Kingdomino already contains a game, or the Kingdomino is null");
		}
		
		Game newGame = new Game(48, kingdomino);
		
		if(kingdomino.getUsers().size() < 2) {
			throw new RuntimeException("There needs to be at least 2 users.");
		}
		
		newGame.setNumberOfPlayers(4);					
		kingdomino.setCurrentGame(newGame);
		
		//add players and dominos and castle
		Player player1 = newGame.getPlayer(0);
		Player player2 = newGame.getPlayer(1);
		Player player3 = newGame.getPlayer(2);
		Player player4 = newGame.getPlayer(3);
		
		newGame.setNextPlayer(player1);//should be randomly determined
		newGame.setNextPlayer(player2);
		newGame.setNextPlayer(player3);
		newGame.setNextPlayer(player4);
		
		Kingdom kingdom1 = new Kingdom(player1);
		Kingdom kingdom2 = new Kingdom(player2);
		Kingdom kingdom3 = new Kingdom(player3);
		Kingdom kingdom4 = new Kingdom(player4);
		
		Castle castle1 = new Castle(0, 0, kingdom1, player1);
		Castle castle2 = new Castle(0, 0, kingdom2, player2);
		Castle castle3 = new Castle(0, 0, kingdom3, player3);
		Castle castle4 = new Castle(0, 0, kingdom4, player4);//check which coordonates this corresponds to
		
		//current settings of the game
		
		KingdominoApplication.setKingdomino(kingdomino);
	}
	//******END FEATURE 3******
	
	//*******BEGIN FEATURE 5*****
	public static List<Domino> shuffleDominos(List<Domino> dominos) {
		//based on number of players in game, number of dominos differ
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game newGame = new Game(48, kingdomino);
		
		dominos = newGame.getAllDominos();
		
		Random r = new Random();
		for (int i=0; i<dominos.size(); i++) {
		    int randomPosition = r.nextInt(dominos.size());
		    Domino d = dominos.get(i);
		    Domino temp = dominos.get(randomPosition);
		    dominos.set(randomPosition, d);
		    dominos.set(i, temp);		    
		}
		
		
		return dominos;
	}
	
}
