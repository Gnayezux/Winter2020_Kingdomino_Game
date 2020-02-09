package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 27 "Kingdomino_i1.ump"
public class Domino
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Direction { Top, Bottom, Left, Right }
  public enum Landscape { Forest, Meadows, Water, Deserts, Mines, Wastelands }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Domino Attributes
  private int number;
  private Direction direction;
  private boolean isSelected;
  private boolean isRevealed;

  //Domino Associations
  private Deck gameDeck;
  private List<DominoTile> tilesOnDomino;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Domino(int aNumber, Direction aDirection, boolean aIsSelected, boolean aIsRevealed, Deck aGameDeck)
  {
    number = aNumber;
    direction = aDirection;
    isSelected = aIsSelected;
    isRevealed = aIsRevealed;
    boolean didAddGameDeck = setGameDeck(aGameDeck);
    if (!didAddGameDeck)
    {
      throw new RuntimeException("Unable to create domino due to gameDeck");
    }
    tilesOnDomino = new ArrayList<DominoTile>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumber(int aNumber)
  {
    boolean wasSet = false;
    number = aNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setDirection(Direction aDirection)
  {
    boolean wasSet = false;
    direction = aDirection;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsSelected(boolean aIsSelected)
  {
    boolean wasSet = false;
    isSelected = aIsSelected;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsRevealed(boolean aIsRevealed)
  {
    boolean wasSet = false;
    isRevealed = aIsRevealed;
    wasSet = true;
    return wasSet;
  }

  public int getNumber()
  {
    return number;
  }

  public Direction getDirection()
  {
    return direction;
  }

  public boolean getIsSelected()
  {
    return isSelected;
  }

  public boolean getIsRevealed()
  {
    return isRevealed;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsSelected()
  {
    return isSelected;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsRevealed()
  {
    return isRevealed;
  }
  /* Code from template association_GetOne */
  public Deck getGameDeck()
  {
    return gameDeck;
  }
  /* Code from template association_GetMany */
  public DominoTile getTilesOnDomino(int index)
  {
    DominoTile aTilesOnDomino = tilesOnDomino.get(index);
    return aTilesOnDomino;
  }

  public List<DominoTile> getTilesOnDomino()
  {
    List<DominoTile> newTilesOnDomino = Collections.unmodifiableList(tilesOnDomino);
    return newTilesOnDomino;
  }

  public int numberOfTilesOnDomino()
  {
    int number = tilesOnDomino.size();
    return number;
  }

  public boolean hasTilesOnDomino()
  {
    boolean has = tilesOnDomino.size() > 0;
    return has;
  }

  public int indexOfTilesOnDomino(DominoTile aTilesOnDomino)
  {
    int index = tilesOnDomino.indexOf(aTilesOnDomino);
    return index;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGameDeck(Deck aGameDeck)
  {
    boolean wasSet = false;
    //Must provide gameDeck to domino
    if (aGameDeck == null)
    {
      return wasSet;
    }

    //gameDeck already at maximum (48)
    if (aGameDeck.numberOfDominos() >= Deck.maximumNumberOfDominos())
    {
      return wasSet;
    }
    
    Deck existingGameDeck = gameDeck;
    gameDeck = aGameDeck;
    if (existingGameDeck != null && !existingGameDeck.equals(aGameDeck))
    {
      boolean didRemove = existingGameDeck.removeDomino(this);
      if (!didRemove)
      {
        gameDeck = existingGameDeck;
        return wasSet;
      }
    }
    gameDeck.addDomino(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfTilesOnDominoValid()
  {
    boolean isValid = numberOfTilesOnDomino() >= minimumNumberOfTilesOnDomino() && numberOfTilesOnDomino() <= maximumNumberOfTilesOnDomino();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfTilesOnDomino()
  {
    return 2;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTilesOnDomino()
  {
    return 2;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfTilesOnDomino()
  {
    return 2;
  }
  /* Code from template association_AddMNToOnlyOne */
  public DominoTile addTilesOnDomino(int aXPosition, int aYPosition, ca.mcgill.ecse223.kingdomino.model.DominoTile.Landscape aLandscape, String aNumOfCrowns, boolean aIsReference, Region aBelongsTo, Kingdom aKingdom)
  {
    if (numberOfTilesOnDomino() >= maximumNumberOfTilesOnDomino())
    {
      return null;
    }
    else
    {
      return new DominoTile(aXPosition, aYPosition, aLandscape, aNumOfCrowns, aIsReference, aBelongsTo, this, aKingdom);
    }
  }

  public boolean addTilesOnDomino(DominoTile aTilesOnDomino)
  {
    boolean wasAdded = false;
    if (tilesOnDomino.contains(aTilesOnDomino)) { return false; }
    if (numberOfTilesOnDomino() >= maximumNumberOfTilesOnDomino())
    {
      return wasAdded;
    }

    Domino existingDomino = aTilesOnDomino.getDomino();
    boolean isNewDomino = existingDomino != null && !this.equals(existingDomino);

    if (isNewDomino && existingDomino.numberOfTilesOnDomino() <= minimumNumberOfTilesOnDomino())
    {
      return wasAdded;
    }

    if (isNewDomino)
    {
      aTilesOnDomino.setDomino(this);
    }
    else
    {
      tilesOnDomino.add(aTilesOnDomino);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTilesOnDomino(DominoTile aTilesOnDomino)
  {
    boolean wasRemoved = false;
    //Unable to remove aTilesOnDomino, as it must always have a domino
    if (this.equals(aTilesOnDomino.getDomino()))
    {
      return wasRemoved;
    }

    //domino already at minimum (2)
    if (numberOfTilesOnDomino() <= minimumNumberOfTilesOnDomino())
    {
      return wasRemoved;
    }
    tilesOnDomino.remove(aTilesOnDomino);
    wasRemoved = true;
    return wasRemoved;
  }

  public void delete()
  {
    Deck placeholderGameDeck = gameDeck;
    this.gameDeck = null;
    if(placeholderGameDeck != null)
    {
      placeholderGameDeck.removeDomino(this);
    }
    while (tilesOnDomino.size() > 0)
    {
      DominoTile aTilesOnDomino = tilesOnDomino.get(tilesOnDomino.size() - 1);
      aTilesOnDomino.delete();
      tilesOnDomino.remove(aTilesOnDomino);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "number" + ":" + getNumber()+ "," +
            "isSelected" + ":" + getIsSelected()+ "," +
            "isRevealed" + ":" + getIsRevealed()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "direction" + "=" + (getDirection() != null ? !getDirection().equals(this)  ? getDirection().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "gameDeck = "+(getGameDeck()!=null?Integer.toHexString(System.identityHashCode(getGameDeck())):"null");
  }
}