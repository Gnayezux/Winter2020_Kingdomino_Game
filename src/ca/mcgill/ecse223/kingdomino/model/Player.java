package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 12 "Kingdomino_i1.ump"
public class Player
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Color { Blue, Red, Green, Yellow }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Player Associations
  private Profile selectedProfile;
  private Kingdom personalKingdom;
  private Game game;
  private List<King> kings;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Player(Profile aSelectedProfile, Kingdom aPersonalKingdom, Game aGame)
  {
    if (aSelectedProfile == null || aSelectedProfile.getPlayer() != null)
    {
      throw new RuntimeException("Unable to create Player due to aSelectedProfile");
    }
    selectedProfile = aSelectedProfile;
    if (aPersonalKingdom == null || aPersonalKingdom.getPlayer() != null)
    {
      throw new RuntimeException("Unable to create Player due to aPersonalKingdom");
    }
    personalKingdom = aPersonalKingdom;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create player due to game");
    }
    kings = new ArrayList<King>();
  }

  public Player(String aUsernameForSelectedProfile, int aWinsForSelectedProfile, int aGamesPlayedForSelectedProfile, int aLossesForSelectedProfile, int aHighscoreForSelectedProfile, int aTotalPointsForSelectedProfile, int aTiesForSelectedProfile, Kingdomino aKingdominoForSelectedProfile, int aTotalScoreForPersonalKingdom, int aCurrentWidthForPersonalKingdom, int aCurrentHeightForPersonalKingdom, int aMaxHeightForPersonalKingdom, int aMaxWidthForPersonalKingdom, CastleTile aCastleForPersonalKingdom, Game aGame)
  {
    selectedProfile = new Profile(aUsernameForSelectedProfile, aWinsForSelectedProfile, aGamesPlayedForSelectedProfile, aLossesForSelectedProfile, aHighscoreForSelectedProfile, aTotalPointsForSelectedProfile, aTiesForSelectedProfile, aKingdominoForSelectedProfile, this);
    personalKingdom = new Kingdom(aTotalScoreForPersonalKingdom, aCurrentWidthForPersonalKingdom, aCurrentHeightForPersonalKingdom, aMaxHeightForPersonalKingdom, aMaxWidthForPersonalKingdom, this, aCastleForPersonalKingdom);
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create player due to game");
    }
    kings = new ArrayList<King>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Profile getSelectedProfile()
  {
    return selectedProfile;
  }
  /* Code from template association_GetOne */
  public Kingdom getPersonalKingdom()
  {
    return personalKingdom;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetMany */
  public King getKing(int index)
  {
    King aKing = kings.get(index);
    return aKing;
  }

  public List<King> getKings()
  {
    List<King> newKings = Collections.unmodifiableList(kings);
    return newKings;
  }

  public int numberOfKings()
  {
    int number = kings.size();
    return number;
  }

  public boolean hasKings()
  {
    boolean has = kings.size() > 0;
    return has;
  }

  public int indexOfKing(King aKing)
  {
    int index = kings.indexOf(aKing);
    return index;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to player
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (4)
    if (aGame.numberOfPlayers() >= Game.maximumNumberOfPlayers())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removePlayer(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addPlayer(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfKingsValid()
  {
    boolean isValid = numberOfKings() >= minimumNumberOfKings() && numberOfKings() <= maximumNumberOfKings();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfKings()
  {
    return 1;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfKings()
  {
    return 2;
  }
  /* Code from template association_AddMNToOnlyOne */
  public King addKing(ca.mcgill.ecse223.kingdomino.model.King.Color aColor, Domino aDominoSelected, Draft aDraft)
  {
    if (numberOfKings() >= maximumNumberOfKings())
    {
      return null;
    }
    else
    {
      return new King(aColor, aDominoSelected, this, aDraft);
    }
  }

  public boolean addKing(King aKing)
  {
    boolean wasAdded = false;
    if (kings.contains(aKing)) { return false; }
    if (numberOfKings() >= maximumNumberOfKings())
    {
      return wasAdded;
    }

    Player existingPlayer = aKing.getPlayer();
    boolean isNewPlayer = existingPlayer != null && !this.equals(existingPlayer);

    if (isNewPlayer && existingPlayer.numberOfKings() <= minimumNumberOfKings())
    {
      return wasAdded;
    }

    if (isNewPlayer)
    {
      aKing.setPlayer(this);
    }
    else
    {
      kings.add(aKing);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeKing(King aKing)
  {
    boolean wasRemoved = false;
    //Unable to remove aKing, as it must always have a player
    if (this.equals(aKing.getPlayer()))
    {
      return wasRemoved;
    }

    //player already at minimum (1)
    if (numberOfKings() <= minimumNumberOfKings())
    {
      return wasRemoved;
    }
    kings.remove(aKing);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addKingAt(King aKing, int index)
  {  
    boolean wasAdded = false;
    if(addKing(aKing))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfKings()) { index = numberOfKings() - 1; }
      kings.remove(aKing);
      kings.add(index, aKing);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveKingAt(King aKing, int index)
  {
    boolean wasAdded = false;
    if(kings.contains(aKing))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfKings()) { index = numberOfKings() - 1; }
      kings.remove(aKing);
      kings.add(index, aKing);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addKingAt(aKing, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Profile existingSelectedProfile = selectedProfile;
    selectedProfile = null;
    if (existingSelectedProfile != null)
    {
      existingSelectedProfile.delete();
    }
    Kingdom existingPersonalKingdom = personalKingdom;
    personalKingdom = null;
    if (existingPersonalKingdom != null)
    {
      existingPersonalKingdom.delete();
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePlayer(this);
    }
    while (kings.size() > 0)
    {
      King aKing = kings.get(kings.size() - 1);
      aKing.delete();
      kings.remove(aKing);
    }
    
  }

}