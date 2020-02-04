/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 27 "../A.ump"
// line 96 "../A.ump"
public class tempStack
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //tempStack Attributes
  private List<dominoTile> tileList;
  private int numTiles;

  //tempStack Associations
  private dominoStack dominoStack;
  private List<player> players;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public tempStack(int aNumTiles, dominoStack aDominoStack)
  {
    tileList = new ArrayList<dominoTile>();
    numTiles = aNumTiles;
    boolean didAddDominoStack = setDominoStack(aDominoStack);
    if (!didAddDominoStack)
    {
      throw new RuntimeException("Unable to create tempStack due to dominoStack");
    }
    players = new ArrayList<player>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template attribute_SetMany */
  public boolean addTileList(dominoTile aTileList)
  {
    boolean wasAdded = false;
    wasAdded = tileList.add(aTileList);
    return wasAdded;
  }

  public boolean removeTileList(dominoTile aTileList)
  {
    boolean wasRemoved = false;
    wasRemoved = tileList.remove(aTileList);
    return wasRemoved;
  }

  public boolean setNumTiles(int aNumTiles)
  {
    boolean wasSet = false;
    numTiles = aNumTiles;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_GetMany */
  public dominoTile getTileList(int index)
  {
    dominoTile aTileList = tileList.get(index);
    return aTileList;
  }

  public dominoTile[] getTileList()
  {
    dominoTile[] newTileList = tileList.toArray(new dominoTile[tileList.size()]);
    return newTileList;
  }

  public int numberOfTileList()
  {
    int number = tileList.size();
    return number;
  }

  public boolean hasTileList()
  {
    boolean has = tileList.size() > 0;
    return has;
  }

  public int indexOfTileList(dominoTile aTileList)
  {
    int index = tileList.indexOf(aTileList);
    return index;
  }

  public int getNumTiles()
  {
    return numTiles;
  }
  /* Code from template association_GetOne */
  public dominoStack getDominoStack()
  {
    return dominoStack;
  }
  /* Code from template association_GetMany */
  public player getPlayer(int index)
  {
    player aPlayer = players.get(index);
    return aPlayer;
  }

  public List<player> getPlayers()
  {
    List<player> newPlayers = Collections.unmodifiableList(players);
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

  public int indexOfPlayer(player aPlayer)
  {
    int index = players.indexOf(aPlayer);
    return index;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setDominoStack(dominoStack aDominoStack)
  {
    boolean wasSet = false;
    //Must provide dominoStack to tempStack
    if (aDominoStack == null)
    {
      return wasSet;
    }

    //dominoStack already at maximum (2)
    if (aDominoStack.numberOfTempStacks() >= dominoStack.maximumNumberOfTempStacks())
    {
      return wasSet;
    }
    
    dominoStack existingDominoStack = dominoStack;
    dominoStack = aDominoStack;
    if (existingDominoStack != null && !existingDominoStack.equals(aDominoStack))
    {
      boolean didRemove = existingDominoStack.removeTempStack(this);
      if (!didRemove)
      {
        dominoStack = existingDominoStack;
        return wasSet;
      }
    }
    dominoStack.addTempStack(this);
    wasSet = true;
    return wasSet;
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
  /* Code from template association_AddManyToManyMethod */
  public boolean addPlayer(player aPlayer)
  {
    boolean wasAdded = false;
    if (players.contains(aPlayer)) { return false; }
    if (numberOfPlayers() >= maximumNumberOfPlayers())
    {
      return wasAdded;
    }

    players.add(aPlayer);
    if (aPlayer.indexOfTempStack(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aPlayer.addTempStack(this);
      if (!wasAdded)
      {
        players.remove(aPlayer);
      }
    }
    return wasAdded;
  }
  /* Code from template association_AddMNToMany */
  public boolean removePlayer(player aPlayer)
  {
    boolean wasRemoved = false;
    if (!players.contains(aPlayer))
    {
      return wasRemoved;
    }

    if (numberOfPlayers() <= minimumNumberOfPlayers())
    {
      return wasRemoved;
    }

    int oldIndex = players.indexOf(aPlayer);
    players.remove(oldIndex);
    if (aPlayer.indexOfTempStack(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aPlayer.removeTempStack(this);
      if (!wasRemoved)
      {
        players.add(oldIndex,aPlayer);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_SetMNToMany */
  public boolean setPlayers(player... newPlayers)
  {
    boolean wasSet = false;
    ArrayList<player> verifiedPlayers = new ArrayList<player>();
    for (player aPlayer : newPlayers)
    {
      if (verifiedPlayers.contains(aPlayer))
      {
        continue;
      }
      verifiedPlayers.add(aPlayer);
    }

    if (verifiedPlayers.size() != newPlayers.length || verifiedPlayers.size() < minimumNumberOfPlayers() || verifiedPlayers.size() > maximumNumberOfPlayers())
    {
      return wasSet;
    }

    ArrayList<player> oldPlayers = new ArrayList<player>(players);
    players.clear();
    for (player aNewPlayer : verifiedPlayers)
    {
      players.add(aNewPlayer);
      if (oldPlayers.contains(aNewPlayer))
      {
        oldPlayers.remove(aNewPlayer);
      }
      else
      {
        aNewPlayer.addTempStack(this);
      }
    }

    for (player anOldPlayer : oldPlayers)
    {
      anOldPlayer.removeTempStack(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPlayerAt(player aPlayer, int index)
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

  public boolean addOrMovePlayerAt(player aPlayer, int index)
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

  public void delete()
  {
    dominoStack placeholderDominoStack = dominoStack;
    this.dominoStack = null;
    if(placeholderDominoStack != null)
    {
      placeholderDominoStack.removeTempStack(this);
    }
    ArrayList<player> copyOfPlayers = new ArrayList<player>(players);
    players.clear();
    for(player aPlayer : copyOfPlayers)
    {
      if (aPlayer.numberOfTempStacks() <= player.minimumNumberOfTempStacks())
      {
        aPlayer.delete();
      }
      else
      {
        aPlayer.removeTempStack(this);
      }
    }
  }

  // line 32 "../A.ump"
   public void pop(){
    
  }

  // line 35 "../A.ump"
   public void add(){
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "numTiles" + ":" + getNumTiles()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "dominoStack = "+(getDominoStack()!=null?Integer.toHexString(System.identityHashCode(getDominoStack())):"null");
  }
}