package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 48 "Kingdomino_i1.ump"
public class Deck
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Direction { Top, Bottom, Left, Right }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Deck Attributes
  private List<Domino> inGameDominos;
  private int size;

  //Deck Associations
  private Game currentGame;
  private List<Domino> dominos;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Deck(int aSize, Game aCurrentGame)
  {
    inGameDominos = new ArrayList<Domino>();
    size = aSize;
    if (aCurrentGame == null || aCurrentGame.getDeck() != null)
    {
      throw new RuntimeException("Unable to create Deck due to aCurrentGame");
    }
    currentGame = aCurrentGame;
    dominos = new ArrayList<Domino>();
  }

  public Deck(int aSize, int aNumPlayersForCurrentGame, int aNumTurnsForCurrentGame, Kingdomino aKingdominoForCurrentGame)
  {
    inGameDominos = new ArrayList<Domino>();
    size = aSize;
    currentGame = new Game(aNumPlayersForCurrentGame, aNumTurnsForCurrentGame, aKingdominoForCurrentGame, this);
    dominos = new ArrayList<Domino>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template attribute_SetMany */
  public boolean addInGameDomino(Domino aInGameDomino)
  {
    boolean wasAdded = false;
    wasAdded = inGameDominos.add(aInGameDomino);
    return wasAdded;
  }

  public boolean removeInGameDomino(Domino aInGameDomino)
  {
    boolean wasRemoved = false;
    wasRemoved = inGameDominos.remove(aInGameDomino);
    return wasRemoved;
  }

  public boolean setSize(int aSize)
  {
    boolean wasSet = false;
    size = aSize;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_GetMany */
  public Domino getInGameDomino(int index)
  {
    Domino aInGameDomino = inGameDominos.get(index);
    return aInGameDomino;
  }

  public Domino[] getInGameDominos()
  {
    Domino[] newInGameDominos = inGameDominos.toArray(new Domino[inGameDominos.size()]);
    return newInGameDominos;
  }

  public int numberOfInGameDominos()
  {
    int number = inGameDominos.size();
    return number;
  }

  public boolean hasInGameDominos()
  {
    boolean has = inGameDominos.size() > 0;
    return has;
  }

  public int indexOfInGameDomino(Domino aInGameDomino)
  {
    int index = inGameDominos.indexOf(aInGameDomino);
    return index;
  }

  public int getSize()
  {
    return size;
  }
  /* Code from template association_GetOne */
  public Game getCurrentGame()
  {
    return currentGame;
  }
  /* Code from template association_GetMany */
  public Domino getDomino(int index)
  {
    Domino aDomino = dominos.get(index);
    return aDomino;
  }

  public List<Domino> getDominos()
  {
    List<Domino> newDominos = Collections.unmodifiableList(dominos);
    return newDominos;
  }

  public int numberOfDominos()
  {
    int number = dominos.size();
    return number;
  }

  public boolean hasDominos()
  {
    boolean has = dominos.size() > 0;
    return has;
  }

  public int indexOfDomino(Domino aDomino)
  {
    int index = dominos.indexOf(aDomino);
    return index;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfDominosValid()
  {
    boolean isValid = numberOfDominos() >= minimumNumberOfDominos() && numberOfDominos() <= maximumNumberOfDominos();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfDominos()
  {
    return 24;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfDominos()
  {
    return 48;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Domino addDomino(int aNumber, ca.mcgill.ecse223.kingdomino.model.Domino.Direction aDirection, boolean aIsSelected, boolean aIsRevealed)
  {
    if (numberOfDominos() >= maximumNumberOfDominos())
    {
      return null;
    }
    else
    {
      return new Domino(aNumber, aDirection, aIsSelected, aIsRevealed, this);
    }
  }

  public boolean addDomino(Domino aDomino)
  {
    boolean wasAdded = false;
    if (dominos.contains(aDomino)) { return false; }
    if (numberOfDominos() >= maximumNumberOfDominos())
    {
      return wasAdded;
    }

    Deck existingGameDeck = aDomino.getGameDeck();
    boolean isNewGameDeck = existingGameDeck != null && !this.equals(existingGameDeck);

    if (isNewGameDeck && existingGameDeck.numberOfDominos() <= minimumNumberOfDominos())
    {
      return wasAdded;
    }

    if (isNewGameDeck)
    {
      aDomino.setGameDeck(this);
    }
    else
    {
      dominos.add(aDomino);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDomino(Domino aDomino)
  {
    boolean wasRemoved = false;
    //Unable to remove aDomino, as it must always have a gameDeck
    if (this.equals(aDomino.getGameDeck()))
    {
      return wasRemoved;
    }

    //gameDeck already at minimum (24)
    if (numberOfDominos() <= minimumNumberOfDominos())
    {
      return wasRemoved;
    }
    dominos.remove(aDomino);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addDominoAt(Domino aDomino, int index)
  {  
    boolean wasAdded = false;
    if(addDomino(aDomino))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDominos()) { index = numberOfDominos() - 1; }
      dominos.remove(aDomino);
      dominos.add(index, aDomino);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDominoAt(Domino aDomino, int index)
  {
    boolean wasAdded = false;
    if(dominos.contains(aDomino))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDominos()) { index = numberOfDominos() - 1; }
      dominos.remove(aDomino);
      dominos.add(index, aDomino);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addDominoAt(aDomino, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Game existingCurrentGame = currentGame;
    currentGame = null;
    if (existingCurrentGame != null)
    {
      existingCurrentGame.delete();
    }
    while (dominos.size() > 0)
    {
      Domino aDomino = dominos.get(dominos.size() - 1);
      aDomino.delete();
      dominos.remove(aDomino);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "size" + ":" + getSize()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "currentGame = "+(getCurrentGame()!=null?Integer.toHexString(System.identityHashCode(getCurrentGame())):"null");
  }
}