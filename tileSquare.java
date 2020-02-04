/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;

// line 70 "../A.ump"
// line 114 "../A.ump"
public class tileSquare
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //tileSquare Attributes
  private String type;
  private int numCrown;

  //tileSquare Associations
  private dominoTile dominoTile;
  private playArea playArea;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public tileSquare(String aType, int aNumCrown, dominoTile aDominoTile, playArea aPlayArea)
  {
    type = aType;
    numCrown = aNumCrown;
    boolean didAddDominoTile = setDominoTile(aDominoTile);
    if (!didAddDominoTile)
    {
      throw new RuntimeException("Unable to create tileSquare due to dominoTile");
    }
    boolean didAddPlayArea = setPlayArea(aPlayArea);
    if (!didAddPlayArea)
    {
      throw new RuntimeException("Unable to create tileSquare due to playArea");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setType(String aType)
  {
    boolean wasSet = false;
    type = aType;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumCrown(int aNumCrown)
  {
    boolean wasSet = false;
    numCrown = aNumCrown;
    wasSet = true;
    return wasSet;
  }

  public String getType()
  {
    return type;
  }

  public int getNumCrown()
  {
    return numCrown;
  }
  /* Code from template association_GetOne */
  public dominoTile getDominoTile()
  {
    return dominoTile;
  }
  /* Code from template association_GetOne */
  public playArea getPlayArea()
  {
    return playArea;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setDominoTile(dominoTile aDominoTile)
  {
    boolean wasSet = false;
    //Must provide dominoTile to tileSquare
    if (aDominoTile == null)
    {
      return wasSet;
    }

    //dominoTile already at maximum (2)
    if (aDominoTile.numberOfTileSquares() >= dominoTile.maximumNumberOfTileSquares())
    {
      return wasSet;
    }
    
    dominoTile existingDominoTile = dominoTile;
    dominoTile = aDominoTile;
    if (existingDominoTile != null && !existingDominoTile.equals(aDominoTile))
    {
      boolean didRemove = existingDominoTile.removeTileSquare(this);
      if (!didRemove)
      {
        dominoTile = existingDominoTile;
        return wasSet;
      }
    }
    dominoTile.addTileSquare(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setPlayArea(playArea aPlayArea)
  {
    boolean wasSet = false;
    //Must provide playArea to tileSquare
    if (aPlayArea == null)
    {
      return wasSet;
    }

    //playArea already at maximum (48)
    if (aPlayArea.numberOfTileSquares() >= playArea.maximumNumberOfTileSquares())
    {
      return wasSet;
    }
    
    playArea existingPlayArea = playArea;
    playArea = aPlayArea;
    if (existingPlayArea != null && !existingPlayArea.equals(aPlayArea))
    {
      boolean didRemove = existingPlayArea.removeTileSquare(this);
      if (!didRemove)
      {
        playArea = existingPlayArea;
        return wasSet;
      }
    }
    playArea.addTileSquare(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    dominoTile placeholderDominoTile = dominoTile;
    this.dominoTile = null;
    if(placeholderDominoTile != null)
    {
      placeholderDominoTile.removeTileSquare(this);
    }
    playArea placeholderPlayArea = playArea;
    this.playArea = null;
    if(placeholderPlayArea != null)
    {
      placeholderPlayArea.removeTileSquare(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "type" + ":" + getType()+ "," +
            "numCrown" + ":" + getNumCrown()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "dominoTile = "+(getDominoTile()!=null?Integer.toHexString(System.identityHashCode(getDominoTile())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "playArea = "+(getPlayArea()!=null?Integer.toHexString(System.identityHashCode(getPlayArea())):"null");
  }
}