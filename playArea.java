/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 59 "../A.ump"
// line 107 "../A.ump"
public class playArea
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //playArea Attributes
  private List<char> board;

  //playArea Associations
  private List<tileSquare> tileSquares;
  private player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public playArea(player aPlayer)
  {
    board = new ArrayList<char>();
    tileSquares = new ArrayList<tileSquare>();
    if (aPlayer == null || aPlayer.getPlayArea() != null)
    {
      throw new RuntimeException("Unable to create playArea due to aPlayer");
    }
    player = aPlayer;
  }

  public playArea(int aScoreForPlayer, String aKingColorForPlayer, castleBlock aCastleBlockForPlayer)
  {
    board = new ArrayList<char>();
    tileSquares = new ArrayList<tileSquare>();
    player = new player(aScoreForPlayer, aKingColorForPlayer, this, aCastleBlockForPlayer);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template attribute_SetMany */
  public boolean addBoard(char aBoard)
  {
    boolean wasAdded = false;
    wasAdded = board.add(aBoard);
    return wasAdded;
  }

  public boolean removeBoard(char aBoard)
  {
    boolean wasRemoved = false;
    wasRemoved = board.remove(aBoard);
    return wasRemoved;
  }
  /* Code from template attribute_GetMany */
  public char getBoard(int index)
  {
    char aBoard = board.get(index);
    return aBoard;
  }

  public char[] getBoard()
  {
    char[] newBoard = board.toArray(new char[board.size()]);
    return newBoard;
  }

  public int numberOfBoard()
  {
    int number = board.size();
    return number;
  }

  public boolean hasBoard()
  {
    boolean has = board.size() > 0;
    return has;
  }

  public int indexOfBoard(char aBoard)
  {
    int index = board.indexOf(aBoard);
    return index;
  }
  /* Code from template association_GetMany */
  public tileSquare getTileSquare(int index)
  {
    tileSquare aTileSquare = tileSquares.get(index);
    return aTileSquare;
  }

  public List<tileSquare> getTileSquares()
  {
    List<tileSquare> newTileSquares = Collections.unmodifiableList(tileSquares);
    return newTileSquares;
  }

  public int numberOfTileSquares()
  {
    int number = tileSquares.size();
    return number;
  }

  public boolean hasTileSquares()
  {
    boolean has = tileSquares.size() > 0;
    return has;
  }

  public int indexOfTileSquare(tileSquare aTileSquare)
  {
    int index = tileSquares.indexOf(aTileSquare);
    return index;
  }
  /* Code from template association_GetOne */
  public player getPlayer()
  {
    return player;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfTileSquaresValid()
  {
    boolean isValid = numberOfTileSquares() >= minimumNumberOfTileSquares() && numberOfTileSquares() <= maximumNumberOfTileSquares();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTileSquares()
  {
    return 24;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfTileSquares()
  {
    return 48;
  }
  /* Code from template association_AddMNToOnlyOne */
  public tileSquare addTileSquare(String aType, int aNumCrown, dominoTile aDominoTile)
  {
    if (numberOfTileSquares() >= maximumNumberOfTileSquares())
    {
      return null;
    }
    else
    {
      return new tileSquare(aType, aNumCrown, aDominoTile, this);
    }
  }

  public boolean addTileSquare(tileSquare aTileSquare)
  {
    boolean wasAdded = false;
    if (tileSquares.contains(aTileSquare)) { return false; }
    if (numberOfTileSquares() >= maximumNumberOfTileSquares())
    {
      return wasAdded;
    }

    playArea existingPlayArea = aTileSquare.getPlayArea();
    boolean isNewPlayArea = existingPlayArea != null && !this.equals(existingPlayArea);

    if (isNewPlayArea && existingPlayArea.numberOfTileSquares() <= minimumNumberOfTileSquares())
    {
      return wasAdded;
    }

    if (isNewPlayArea)
    {
      aTileSquare.setPlayArea(this);
    }
    else
    {
      tileSquares.add(aTileSquare);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTileSquare(tileSquare aTileSquare)
  {
    boolean wasRemoved = false;
    //Unable to remove aTileSquare, as it must always have a playArea
    if (this.equals(aTileSquare.getPlayArea()))
    {
      return wasRemoved;
    }

    //playArea already at minimum (24)
    if (numberOfTileSquares() <= minimumNumberOfTileSquares())
    {
      return wasRemoved;
    }
    tileSquares.remove(aTileSquare);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTileSquareAt(tileSquare aTileSquare, int index)
  {  
    boolean wasAdded = false;
    if(addTileSquare(aTileSquare))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTileSquares()) { index = numberOfTileSquares() - 1; }
      tileSquares.remove(aTileSquare);
      tileSquares.add(index, aTileSquare);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTileSquareAt(tileSquare aTileSquare, int index)
  {
    boolean wasAdded = false;
    if(tileSquares.contains(aTileSquare))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTileSquares()) { index = numberOfTileSquares() - 1; }
      tileSquares.remove(aTileSquare);
      tileSquares.add(index, aTileSquare);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTileSquareAt(aTileSquare, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=tileSquares.size(); i > 0; i--)
    {
      tileSquare aTileSquare = tileSquares.get(i - 1);
      aTileSquare.delete();
    }
    player existingPlayer = player;
    player = null;
    if (existingPlayer != null)
    {
      existingPlayer.delete();
    }
  }

  // line 65 "../A.ump"
   public void putTile(){
    
  }

  // line 68 "../A.ump"
   public void boardScore(){
    
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null");
  }
}