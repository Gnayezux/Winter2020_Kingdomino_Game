package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 6 "Kingdomino_i1.ump"
public class Game
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private int numPlayers;
  private int numTurns;

  //Game Associations
  private List<Player> players;
  private Kingdomino kingdomino;
  private Deck deck;
  private List<Draft> draftsOnBoard;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(int aNumPlayers, int aNumTurns, Kingdomino aKingdomino, Deck aDeck)
  {
    numPlayers = aNumPlayers;
    numTurns = aNumTurns;
    players = new ArrayList<Player>();
    if (aKingdomino == null || aKingdomino.getCurrentGame() != null)
    {
      throw new RuntimeException("Unable to create Game due to aKingdomino");
    }
    kingdomino = aKingdomino;
    if (aDeck == null || aDeck.getCurrentGame() != null)
    {
      throw new RuntimeException("Unable to create Game due to aDeck");
    }
    deck = aDeck;
    draftsOnBoard = new ArrayList<Draft>();
  }

  public Game(int aNumPlayers, int aNumTurns, int aSizeForDeck)
  {
    numPlayers = aNumPlayers;
    numTurns = aNumTurns;
    players = new ArrayList<Player>();
    kingdomino = new Kingdomino(this);
    deck = new Deck(aSizeForDeck, this);
    draftsOnBoard = new ArrayList<Draft>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumPlayers(int aNumPlayers)
  {
    boolean wasSet = false;
    numPlayers = aNumPlayers;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumTurns(int aNumTurns)
  {
    boolean wasSet = false;
    numTurns = aNumTurns;
    wasSet = true;
    return wasSet;
  }

  public int getNumPlayers()
  {
    return numPlayers;
  }

  public int getNumTurns()
  {
    return numTurns;
  }
  /* Code from template association_GetMany */
  public Player getPlayer(int index)
  {
    Player aPlayer = players.get(index);
    return aPlayer;
  }

  public List<Player> getPlayers()
  {
    List<Player> newPlayers = Collections.unmodifiableList(players);
    return newPlayers;
  }

  public int numberOfPlayers()
  {
    int number = players.size();
    return number;
  }

  public boolean hasPlayers()
  {
    boolean has = players.size() > 0;
    return has;
  }

  public int indexOfPlayer(Player aPlayer)
  {
    int index = players.indexOf(aPlayer);
    return index;
  }
  /* Code from template association_GetOne */
  public Kingdomino getKingdomino()
  {
    return kingdomino;
  }
  /* Code from template association_GetOne */
  public Deck getDeck()
  {
    return deck;
  }
  /* Code from template association_GetMany */
  public Draft getDraftsOnBoard(int index)
  {
    Draft aDraftsOnBoard = draftsOnBoard.get(index);
    return aDraftsOnBoard;
  }

  public List<Draft> getDraftsOnBoard()
  {
    List<Draft> newDraftsOnBoard = Collections.unmodifiableList(draftsOnBoard);
    return newDraftsOnBoard;
  }

  public int numberOfDraftsOnBoard()
  {
    int number = draftsOnBoard.size();
    return number;
  }

  public boolean hasDraftsOnBoard()
  {
    boolean has = draftsOnBoard.size() > 0;
    return has;
  }

  public int indexOfDraftsOnBoard(Draft aDraftsOnBoard)
  {
    int index = draftsOnBoard.indexOf(aDraftsOnBoard);
    return index;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfPlayersValid()
  {
    boolean isValid = numberOfPlayers() >= minimumNumberOfPlayers() && numberOfPlayers() <= maximumNumberOfPlayers();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlayers()
  {
    return 2;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfPlayers()
  {
    return 4;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Player addPlayer(Profile aSelectedProfile, Kingdom aPersonalKingdom)
  {
    if (numberOfPlayers() >= maximumNumberOfPlayers())
    {
      return null;
    }
    else
    {
      return new Player(aSelectedProfile, aPersonalKingdom, this);
    }
  }

  public boolean addPlayer(Player aPlayer)
  {
    boolean wasAdded = false;
    if (players.contains(aPlayer)) { return false; }
    if (numberOfPlayers() >= maximumNumberOfPlayers())
    {
      return wasAdded;
    }

    Game existingGame = aPlayer.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);

    if (isNewGame && existingGame.numberOfPlayers() <= minimumNumberOfPlayers())
    {
      return wasAdded;
    }

    if (isNewGame)
    {
      aPlayer.setGame(this);
    }
    else
    {
      players.add(aPlayer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePlayer(Player aPlayer)
  {
    boolean wasRemoved = false;
    //Unable to remove aPlayer, as it must always have a game
    if (this.equals(aPlayer.getGame()))
    {
      return wasRemoved;
    }

    //game already at minimum (2)
    if (numberOfPlayers() <= minimumNumberOfPlayers())
    {
      return wasRemoved;
    }
    players.remove(aPlayer);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPlayerAt(Player aPlayer, int index)
  {  
    boolean wasAdded = false;
    if(addPlayer(aPlayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayers()) { index = numberOfPlayers() - 1; }
      players.remove(aPlayer);
      players.add(index, aPlayer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePlayerAt(Player aPlayer, int index)
  {
    boolean wasAdded = false;
    if(players.contains(aPlayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayers()) { index = numberOfPlayers() - 1; }
      players.remove(aPlayer);
      players.add(index, aPlayer);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPlayerAt(aPlayer, index);
    }
    return wasAdded;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfDraftsOnBoardValid()
  {
    boolean isValid = numberOfDraftsOnBoard() >= minimumNumberOfDraftsOnBoard() && numberOfDraftsOnBoard() <= maximumNumberOfDraftsOnBoard();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfDraftsOnBoard()
  {
    return 1;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfDraftsOnBoard()
  {
    return 2;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Draft addDraftsOnBoard()
  {
    if (numberOfDraftsOnBoard() >= maximumNumberOfDraftsOnBoard())
    {
      return null;
    }
    else
    {
      return new Draft(this);
    }
  }

  public boolean addDraftsOnBoard(Draft aDraftsOnBoard)
  {
    boolean wasAdded = false;
    if (draftsOnBoard.contains(aDraftsOnBoard)) { return false; }
    if (numberOfDraftsOnBoard() >= maximumNumberOfDraftsOnBoard())
    {
      return wasAdded;
    }

    Game existingCurrentGame = aDraftsOnBoard.getCurrentGame();
    boolean isNewCurrentGame = existingCurrentGame != null && !this.equals(existingCurrentGame);

    if (isNewCurrentGame && existingCurrentGame.numberOfDraftsOnBoard() <= minimumNumberOfDraftsOnBoard())
    {
      return wasAdded;
    }

    if (isNewCurrentGame)
    {
      aDraftsOnBoard.setCurrentGame(this);
    }
    else
    {
      draftsOnBoard.add(aDraftsOnBoard);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDraftsOnBoard(Draft aDraftsOnBoard)
  {
    boolean wasRemoved = false;
    //Unable to remove aDraftsOnBoard, as it must always have a currentGame
    if (this.equals(aDraftsOnBoard.getCurrentGame()))
    {
      return wasRemoved;
    }

    //currentGame already at minimum (1)
    if (numberOfDraftsOnBoard() <= minimumNumberOfDraftsOnBoard())
    {
      return wasRemoved;
    }
    draftsOnBoard.remove(aDraftsOnBoard);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addDraftsOnBoardAt(Draft aDraftsOnBoard, int index)
  {  
    boolean wasAdded = false;
    if(addDraftsOnBoard(aDraftsOnBoard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDraftsOnBoard()) { index = numberOfDraftsOnBoard() - 1; }
      draftsOnBoard.remove(aDraftsOnBoard);
      draftsOnBoard.add(index, aDraftsOnBoard);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDraftsOnBoardAt(Draft aDraftsOnBoard, int index)
  {
    boolean wasAdded = false;
    if(draftsOnBoard.contains(aDraftsOnBoard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDraftsOnBoard()) { index = numberOfDraftsOnBoard() - 1; }
      draftsOnBoard.remove(aDraftsOnBoard);
      draftsOnBoard.add(index, aDraftsOnBoard);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addDraftsOnBoardAt(aDraftsOnBoard, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (players.size() > 0)
    {
      Player aPlayer = players.get(players.size() - 1);
      aPlayer.delete();
      players.remove(aPlayer);
    }
    
    Kingdomino existingKingdomino = kingdomino;
    kingdomino = null;
    if (existingKingdomino != null)
    {
      existingKingdomino.delete();
    }
    Deck existingDeck = deck;
    deck = null;
    if (existingDeck != null)
    {
      existingDeck.delete();
    }
    while (draftsOnBoard.size() > 0)
    {
      Draft aDraftsOnBoard = draftsOnBoard.get(draftsOnBoard.size() - 1);
      aDraftsOnBoard.delete();
      draftsOnBoard.remove(aDraftsOnBoard);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "numPlayers" + ":" + getNumPlayers()+ "," +
            "numTurns" + ":" + getNumTurns()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "kingdomino = "+(getKingdomino()!=null?Integer.toHexString(System.identityHashCode(getKingdomino())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "deck = "+(getDeck()!=null?Integer.toHexString(System.identityHashCode(getDeck())):"null");
  }
}