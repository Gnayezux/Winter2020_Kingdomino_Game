package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 67 "Kingdomino_i1.ump"
public class Draft
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Color { Blue, Red, Green, Yellow }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Draft Attributes
  private List<Domino> slots;

  //Draft Associations
  private Game currentGame;
  private List<King> kings;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Draft(Game aCurrentGame)
  {
    slots = new ArrayList<Domino>();
    boolean didAddCurrentGame = setCurrentGame(aCurrentGame);
    if (!didAddCurrentGame)
    {
      throw new RuntimeException("Unable to create draftsOnBoard due to currentGame");
    }
    kings = new ArrayList<King>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template attribute_SetMany */
  public boolean addSlot(Domino aSlot)
  {
    boolean wasAdded = false;
    wasAdded = slots.add(aSlot);
    return wasAdded;
  }

  public boolean removeSlot(Domino aSlot)
  {
    boolean wasRemoved = false;
    wasRemoved = slots.remove(aSlot);
    return wasRemoved;
  }
  /* Code from template attribute_GetMany */
  public Domino getSlot(int index)
  {
    Domino aSlot = slots.get(index);
    return aSlot;
  }

  public Domino[] getSlots()
  {
    Domino[] newSlots = slots.toArray(new Domino[slots.size()]);
    return newSlots;
  }

  public int numberOfSlots()
  {
    int number = slots.size();
    return number;
  }

  public boolean hasSlots()
  {
    boolean has = slots.size() > 0;
    return has;
  }

  public int indexOfSlot(Domino aSlot)
  {
    int index = slots.indexOf(aSlot);
    return index;
  }
  /* Code from template association_GetOne */
  public Game getCurrentGame()
  {
    return currentGame;
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
  public boolean setCurrentGame(Game aCurrentGame)
  {
    boolean wasSet = false;
    //Must provide currentGame to draftsOnBoard
    if (aCurrentGame == null)
    {
      return wasSet;
    }

    //currentGame already at maximum (2)
    if (aCurrentGame.numberOfDraftsOnBoard() >= Game.maximumNumberOfDraftsOnBoard())
    {
      return wasSet;
    }
    
    Game existingCurrentGame = currentGame;
    currentGame = aCurrentGame;
    if (existingCurrentGame != null && !existingCurrentGame.equals(aCurrentGame))
    {
      boolean didRemove = existingCurrentGame.removeDraftsOnBoard(this);
      if (!didRemove)
      {
        currentGame = existingCurrentGame;
        return wasSet;
      }
    }
    currentGame.addDraftsOnBoard(this);
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
    return 2;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfKings()
  {
    return 4;
  }
  /* Code from template association_AddMNToOnlyOne */
  public King addKing(ca.mcgill.ecse223.kingdomino.model.King.Color aColor, Domino aDominoSelected, Player aPlayer)
  {
    if (numberOfKings() >= maximumNumberOfKings())
    {
      return null;
    }
    else
    {
      return new King(aColor, aDominoSelected, aPlayer, this);
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

    Draft existingDraft = aKing.getDraft();
    boolean isNewDraft = existingDraft != null && !this.equals(existingDraft);

    if (isNewDraft && existingDraft.numberOfKings() <= minimumNumberOfKings())
    {
      return wasAdded;
    }

    if (isNewDraft)
    {
      aKing.setDraft(this);
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
    //Unable to remove aKing, as it must always have a draft
    if (this.equals(aKing.getDraft()))
    {
      return wasRemoved;
    }

    //draft already at minimum (2)
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
    Game placeholderCurrentGame = currentGame;
    this.currentGame = null;
    if(placeholderCurrentGame != null)
    {
      placeholderCurrentGame.removeDraftsOnBoard(this);
    }
    for(int i=kings.size(); i > 0; i--)
    {
      King aKing = kings.get(i - 1);
      aKing.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "currentGame = "+(getCurrentGame()!=null?Integer.toHexString(System.identityHashCode(getCurrentGame())):"null");
  }
}