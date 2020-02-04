/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 37 "../A.ump"
// line 101 "../A.ump"
public class player
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //player Attributes
  private int score;
  private List<dominoTile> tileDrawn;
  private String kingColor;

  //player Associations
  private List<tempStack> tempStacks;
  private playArea playArea;
  private castleBlock castleBlock;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public player(int aScore, String aKingColor, playArea aPlayArea, castleBlock aCastleBlock)
  {
    score = aScore;
    tileDrawn = new ArrayList<dominoTile>();
    kingColor = aKingColor;
    tempStacks = new ArrayList<tempStack>();
    if (aPlayArea == null || aPlayArea.getPlayer() != null)
    {
      throw new RuntimeException("Unable to create player due to aPlayArea");
    }
    playArea = aPlayArea;
    if (aCastleBlock == null || aCastleBlock.getPlayer() != null)
    {
      throw new RuntimeException("Unable to create player due to aCastleBlock");
    }
    castleBlock = aCastleBlock;
  }

  public player(int aScore, String aKingColor, player aPlayer_ForCastleBlock)
  {
    score = aScore;
    tileDrawn = new ArrayList<dominoTile>();
    kingColor = aKingColor;
    tempStacks = new ArrayList<tempStack>();
    playArea = new playArea(this);
    castleBlock = new castleBlock(aPlayer_ForCastleBlock, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setScore(int aScore)
  {
    boolean wasSet = false;
    score = aScore;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetMany */
  public boolean addTileDrawn(dominoTile aTileDrawn)
  {
    boolean wasAdded = false;
    wasAdded = tileDrawn.add(aTileDrawn);
    return wasAdded;
  }

  public boolean removeTileDrawn(dominoTile aTileDrawn)
  {
    boolean wasRemoved = false;
    wasRemoved = tileDrawn.remove(aTileDrawn);
    return wasRemoved;
  }

  public boolean setKingColor(String aKingColor)
  {
    boolean wasSet = false;
    kingColor = aKingColor;
    wasSet = true;
    return wasSet;
  }

  public int getScore()
  {
    return score;
  }
  /* Code from template attribute_GetMany */
  public dominoTile getTileDrawn(int index)
  {
    dominoTile aTileDrawn = tileDrawn.get(index);
    return aTileDrawn;
  }

  public dominoTile[] getTileDrawn()
  {
    dominoTile[] newTileDrawn = tileDrawn.toArray(new dominoTile[tileDrawn.size()]);
    return newTileDrawn;
  }

  public int numberOfTileDrawn()
  {
    int number = tileDrawn.size();
    return number;
  }

  public boolean hasTileDrawn()
  {
    boolean has = tileDrawn.size() > 0;
    return has;
  }

  public int indexOfTileDrawn(dominoTile aTileDrawn)
  {
    int index = tileDrawn.indexOf(aTileDrawn);
    return index;
  }

  public String getKingColor()
  {
    return kingColor;
  }
  /* Code from template association_GetMany */
  public tempStack getTempStack(int index)
  {
    tempStack aTempStack = tempStacks.get(index);
    return aTempStack;
  }

  public List<tempStack> getTempStacks()
  {
    List<tempStack> newTempStacks = Collections.unmodifiableList(tempStacks);
    return newTempStacks;
  }

  public int numberOfTempStacks()
  {
    int number = tempStacks.size();
    return number;
  }

  public boolean hasTempStacks()
  {
    boolean has = tempStacks.size() > 0;
    return has;
  }

  public int indexOfTempStack(tempStack aTempStack)
  {
    int index = tempStacks.indexOf(aTempStack);
    return index;
  }
  /* Code from template association_GetOne */
  public playArea getPlayArea()
  {
    return playArea;
  }
  /* Code from template association_GetOne */
  public castleBlock getCastleBlock()
  {
    return castleBlock;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfTempStacksValid()
  {
    boolean isValid = numberOfTempStacks() >= minimumNumberOfTempStacks() && numberOfTempStacks() <= maximumNumberOfTempStacks();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTempStacks()
  {
    return 1;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfTempStacks()
  {
    return 2;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addTempStack(tempStack aTempStack)
  {
    boolean wasAdded = false;
    if (tempStacks.contains(aTempStack)) { return false; }
    if (numberOfTempStacks() >= maximumNumberOfTempStacks())
    {
      return wasAdded;
    }

    tempStacks.add(aTempStack);
    if (aTempStack.indexOfPlayer(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aTempStack.addPlayer(this);
      if (!wasAdded)
      {
        tempStacks.remove(aTempStack);
      }
    }
    return wasAdded;
  }
  /* Code from template association_AddMNToMany */
  public boolean removeTempStack(tempStack aTempStack)
  {
    boolean wasRemoved = false;
    if (!tempStacks.contains(aTempStack))
    {
      return wasRemoved;
    }

    if (numberOfTempStacks() <= minimumNumberOfTempStacks())
    {
      return wasRemoved;
    }

    int oldIndex = tempStacks.indexOf(aTempStack);
    tempStacks.remove(oldIndex);
    if (aTempStack.indexOfPlayer(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aTempStack.removePlayer(this);
      if (!wasRemoved)
      {
        tempStacks.add(oldIndex,aTempStack);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_SetMNToMany */
  public boolean setTempStacks(tempStack... newTempStacks)
  {
    boolean wasSet = false;
    ArrayList<tempStack> verifiedTempStacks = new ArrayList<tempStack>();
    for (tempStack aTempStack : newTempStacks)
    {
      if (verifiedTempStacks.contains(aTempStack))
      {
        continue;
      }
      verifiedTempStacks.add(aTempStack);
    }

    if (verifiedTempStacks.size() != newTempStacks.length || verifiedTempStacks.size() < minimumNumberOfTempStacks() || verifiedTempStacks.size() > maximumNumberOfTempStacks())
    {
      return wasSet;
    }

    ArrayList<tempStack> oldTempStacks = new ArrayList<tempStack>(tempStacks);
    tempStacks.clear();
    for (tempStack aNewTempStack : verifiedTempStacks)
    {
      tempStacks.add(aNewTempStack);
      if (oldTempStacks.contains(aNewTempStack))
      {
        oldTempStacks.remove(aNewTempStack);
      }
      else
      {
        aNewTempStack.addPlayer(this);
      }
    }

    for (tempStack anOldTempStack : oldTempStacks)
    {
      anOldTempStack.removePlayer(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTempStackAt(tempStack aTempStack, int index)
  {  
    boolean wasAdded = false;
    if(addTempStack(aTempStack))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTempStacks()) { index = numberOfTempStacks() - 1; }
      tempStacks.remove(aTempStack);
      tempStacks.add(index, aTempStack);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTempStackAt(tempStack aTempStack, int index)
  {
    boolean wasAdded = false;
    if(tempStacks.contains(aTempStack))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTempStacks()) { index = numberOfTempStacks() - 1; }
      tempStacks.remove(aTempStack);
      tempStacks.add(index, aTempStack);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTempStackAt(aTempStack, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ArrayList<tempStack> copyOfTempStacks = new ArrayList<tempStack>(tempStacks);
    tempStacks.clear();
    for(tempStack aTempStack : copyOfTempStacks)
    {
      if (aTempStack.numberOfPlayers() <= tempStack.minimumNumberOfPlayers())
      {
        aTempStack.delete();
      }
      else
      {
        aTempStack.removePlayer(this);
      }
    }
    playArea existingPlayArea = playArea;
    playArea = null;
    if (existingPlayArea != null)
    {
      existingPlayArea.delete();
    }
    castleBlock existingCastleBlock = castleBlock;
    castleBlock = null;
    if (existingCastleBlock != null)
    {
      existingCastleBlock.delete();
    }
  }

  // line 45 "../A.ump"
   public dominoTile selectTile(dominoTiles tile){
    
  }

  // line 48 "../A.ump"
   public dominoTile placeTile(dominoTile tile){
    
  }

  // line 51 "../A.ump"
   public dominoTile discardTile(dominoTile arg0){
    
  }

  // line 54 "../A.ump"
   public void calculateScore(){
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "score" + ":" + getScore()+ "," +
            "kingColor" + ":" + getKingColor()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "playArea = "+(getPlayArea()!=null?Integer.toHexString(System.identityHashCode(getPlayArea())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "castleBlock = "+(getCastleBlock()!=null?Integer.toHexString(System.identityHashCode(getCastleBlock())):"null");
  }
}