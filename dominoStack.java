/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 13 "../A.ump"
// line 90 "../A.ump"
public class dominoStack
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //dominoStack Attributes
  private int tileRemaining;
  private tempStack tileList1;
  private tempStack tileList2;

  //dominoStack Associations
  private List<tempStack> tempStacks;
  private List<dominoTile> dT;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public dominoStack(int aTileRemaining, tempStack aTileList1, tempStack aTileList2)
  {
    tileRemaining = aTileRemaining;
    tileList1 = aTileList1;
    tileList2 = aTileList2;
    tempStacks = new ArrayList<tempStack>();
    dT = new ArrayList<dominoTile>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTileRemaining(int aTileRemaining)
  {
    boolean wasSet = false;
    tileRemaining = aTileRemaining;
    wasSet = true;
    return wasSet;
  }

  public boolean setTileList1(tempStack aTileList1)
  {
    boolean wasSet = false;
    tileList1 = aTileList1;
    wasSet = true;
    return wasSet;
  }

  public boolean setTileList2(tempStack aTileList2)
  {
    boolean wasSet = false;
    tileList2 = aTileList2;
    wasSet = true;
    return wasSet;
  }

  public int getTileRemaining()
  {
    return tileRemaining;
  }

  public tempStack getTileList1()
  {
    return tileList1;
  }

  public tempStack getTileList2()
  {
    return tileList2;
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
  /* Code from template association_GetMany */
  public dominoTile getDT(int index)
  {
    dominoTile aDT = dT.get(index);
    return aDT;
  }

  public List<dominoTile> getDT()
  {
    List<dominoTile> newDT = Collections.unmodifiableList(dT);
    return newDT;
  }

  public int numberOfDT()
  {
    int number = dT.size();
    return number;
  }

  public boolean hasDT()
  {
    boolean has = dT.size() > 0;
    return has;
  }

  public int indexOfDT(dominoTile aDT)
  {
    int index = dT.indexOf(aDT);
    return index;
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
  /* Code from template association_AddMNToOnlyOne */
  public tempStack addTempStack(int aNumTiles)
  {
    if (numberOfTempStacks() >= maximumNumberOfTempStacks())
    {
      return null;
    }
    else
    {
      return new tempStack(aNumTiles, this);
    }
  }

  public boolean addTempStack(tempStack aTempStack)
  {
    boolean wasAdded = false;
    if (tempStacks.contains(aTempStack)) { return false; }
    if (numberOfTempStacks() >= maximumNumberOfTempStacks())
    {
      return wasAdded;
    }

    dominoStack existingDominoStack = aTempStack.getDominoStack();
    boolean isNewDominoStack = existingDominoStack != null && !this.equals(existingDominoStack);

    if (isNewDominoStack && existingDominoStack.numberOfTempStacks() <= minimumNumberOfTempStacks())
    {
      return wasAdded;
    }

    if (isNewDominoStack)
    {
      aTempStack.setDominoStack(this);
    }
    else
    {
      tempStacks.add(aTempStack);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTempStack(tempStack aTempStack)
  {
    boolean wasRemoved = false;
    //Unable to remove aTempStack, as it must always have a dominoStack
    if (this.equals(aTempStack.getDominoStack()))
    {
      return wasRemoved;
    }

    //dominoStack already at minimum (1)
    if (numberOfTempStacks() <= minimumNumberOfTempStacks())
    {
      return wasRemoved;
    }
    tempStacks.remove(aTempStack);
    wasRemoved = true;
    return wasRemoved;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfDT()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfDT()
  {
    return 48;
  }
  /* Code from template association_AddOptionalNToOptionalOne */
  public boolean addDT(dominoTile aDT)
  {
    boolean wasAdded = false;
    if (dT.contains(aDT)) { return false; }
    if (numberOfDT() >= maximumNumberOfDT())
    {
      return wasAdded;
    }

    dominoStack existingD = aDT.getD();
    if (existingD == null)
    {
      aDT.setD(this);
    }
    else if (!this.equals(existingD))
    {
      existingD.removeDT(aDT);
      addDT(aDT);
    }
    else
    {
      dT.add(aDT);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDT(dominoTile aDT)
  {
    boolean wasRemoved = false;
    if (dT.contains(aDT))
    {
      dT.remove(aDT);
      aDT.setD(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addDTAt(dominoTile aDT, int index)
  {  
    boolean wasAdded = false;
    if(addDT(aDT))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDT()) { index = numberOfDT() - 1; }
      dT.remove(aDT);
      dT.add(index, aDT);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDTAt(dominoTile aDT, int index)
  {
    boolean wasAdded = false;
    if(dT.contains(aDT))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDT()) { index = numberOfDT() - 1; }
      dT.remove(aDT);
      dT.add(index, aDT);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addDTAt(aDT, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=tempStacks.size(); i > 0; i--)
    {
      tempStack aTempStack = tempStacks.get(i - 1);
      aTempStack.delete();
    }
    while (dT.size() > 0)
    {
      dominoTile aDT = dT.get(dT.size() - 1);
      aDT.delete();
      dT.remove(aDT);
    }
    
  }

  // line 21 "../A.ump"
   public void gameStart(){
    
  }

  // line 24 "../A.ump"
   public dominoTile pop(dominoTile tile){
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "tileRemaining" + ":" + getTileRemaining()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "tileList1" + "=" + (getTileList1() != null ? !getTileList1().equals(this)  ? getTileList1().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "tileList2" + "=" + (getTileList2() != null ? !getTileList2().equals(this)  ? getTileList2().toString().replaceAll("  ","    ") : "this" : "null");
  }
}