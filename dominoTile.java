/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 1 "../A.ump"
// line 84 "../A.ump"
public class dominoTile
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //dominoTile Attributes
  private int numCrown;
  private tileSquare square1;
  private tileSquare square2;
  private int tileNumber;

  //dominoTile Associations
  private List<tileSquare> tileSquares;
  private dominoStack d;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public dominoTile(int aNumCrown, tileSquare aSquare1, tileSquare aSquare2, int aTileNumber)
  {
    numCrown = aNumCrown;
    square1 = aSquare1;
    square2 = aSquare2;
    tileNumber = aTileNumber;
    tileSquares = new ArrayList<tileSquare>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumCrown(int aNumCrown)
  {
    boolean wasSet = false;
    numCrown = aNumCrown;
    wasSet = true;
    return wasSet;
  }

  public boolean setSquare1(tileSquare aSquare1)
  {
    boolean wasSet = false;
    square1 = aSquare1;
    wasSet = true;
    return wasSet;
  }

  public boolean setSquare2(tileSquare aSquare2)
  {
    boolean wasSet = false;
    square2 = aSquare2;
    wasSet = true;
    return wasSet;
  }

  public boolean setTileNumber(int aTileNumber)
  {
    boolean wasSet = false;
    tileNumber = aTileNumber;
    wasSet = true;
    return wasSet;
  }

  public int getNumCrown()
  {
    return numCrown;
  }

  public tileSquare getSquare1()
  {
    return square1;
  }

  public tileSquare getSquare2()
  {
    return square2;
  }

  public int getTileNumber()
  {
    return tileNumber;
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
  public dominoStack getD()
  {
    return d;
  }

  public boolean hasD()
  {
    boolean has = d != null;
    return has;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfTileSquaresValid()
  {
    boolean isValid = numberOfTileSquares() >= minimumNumberOfTileSquares() && numberOfTileSquares() <= maximumNumberOfTileSquares();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfTileSquares()
  {
    return 2;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTileSquares()
  {
    return 2;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfTileSquares()
  {
    return 2;
  }
  /* Code from template association_AddMNToOnlyOne */
  public tileSquare addTileSquare(String aType, int aNumCrown, playArea aPlayArea)
  {
    if (numberOfTileSquares() >= maximumNumberOfTileSquares())
    {
      return null;
    }
    else
    {
      return new tileSquare(aType, aNumCrown, this, aPlayArea);
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

    dominoTile existingDominoTile = aTileSquare.getDominoTile();
    boolean isNewDominoTile = existingDominoTile != null && !this.equals(existingDominoTile);

    if (isNewDominoTile && existingDominoTile.numberOfTileSquares() <= minimumNumberOfTileSquares())
    {
      return wasAdded;
    }

    if (isNewDominoTile)
    {
      aTileSquare.setDominoTile(this);
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
    //Unable to remove aTileSquare, as it must always have a dominoTile
    if (this.equals(aTileSquare.getDominoTile()))
    {
      return wasRemoved;
    }

    //dominoTile already at minimum (2)
    if (numberOfTileSquares() <= minimumNumberOfTileSquares())
    {
      return wasRemoved;
    }
    tileSquares.remove(aTileSquare);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_SetOptionalOneToOptionalN */
  public boolean setD(dominoStack aD)
  {
    boolean wasSet = false;
    if (aD != null && aD.numberOfDT() >= dominoStack.maximumNumberOfDT())
    {
      return wasSet;
    }

    dominoStack existingD = d;
    d = aD;
    if (existingD != null && !existingD.equals(aD))
    {
      existingD.removeDT(this);
    }
    if (aD != null)
    {
      aD.addDT(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    for(int i=tileSquares.size(); i > 0; i--)
    {
      tileSquare aTileSquare = tileSquares.get(i - 1);
      aTileSquare.delete();
    }
    if (d != null)
    {
      dominoStack placeholderD = d;
      this.d = null;
      placeholderD.removeDT(this);
    }
  }

  // line 10 "../A.ump"
   public void flipSelf(){
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "numCrown" + ":" + getNumCrown()+ "," +
            "tileNumber" + ":" + getTileNumber()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "square1" + "=" + (getSquare1() != null ? !getSquare1().equals(this)  ? getSquare1().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "square2" + "=" + (getSquare2() != null ? !getSquare2().equals(this)  ? getSquare2().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "d = "+(getD()!=null?Integer.toHexString(System.identityHashCode(getD())):"null");
  }
}