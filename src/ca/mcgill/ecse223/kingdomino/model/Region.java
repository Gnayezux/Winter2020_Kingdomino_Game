package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 73 "Kingdomino_i1.ump"
public class Region
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Landscape { Forest, Meadows, Water, Deserts, Mines, Wastelands }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Region Attributes
  private Landscape landscape;
  private int size;
  private int numCrowns;

  //Region Associations
  private Kingdom kingdom;
  private List<DominoTile> dominoTiles;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Region(Landscape aLandscape, int aSize, int aNumCrowns, Kingdom aKingdom)
  {
    landscape = aLandscape;
    size = aSize;
    numCrowns = aNumCrowns;
    boolean didAddKingdom = setKingdom(aKingdom);
    if (!didAddKingdom)
    {
      throw new RuntimeException("Unable to create region due to kingdom");
    }
    dominoTiles = new ArrayList<DominoTile>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLandscape(Landscape aLandscape)
  {
    boolean wasSet = false;
    landscape = aLandscape;
    wasSet = true;
    return wasSet;
  }

  public boolean setSize(int aSize)
  {
    boolean wasSet = false;
    size = aSize;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumCrowns(int aNumCrowns)
  {
    boolean wasSet = false;
    numCrowns = aNumCrowns;
    wasSet = true;
    return wasSet;
  }

  public Landscape getLandscape()
  {
    return landscape;
  }

  public int getSize()
  {
    return size;
  }

  public int getNumCrowns()
  {
    return numCrowns;
  }
  /* Code from template association_GetOne */
  public Kingdom getKingdom()
  {
    return kingdom;
  }
  /* Code from template association_GetMany */
  public DominoTile getDominoTile(int index)
  {
    DominoTile aDominoTile = dominoTiles.get(index);
    return aDominoTile;
  }

  public List<DominoTile> getDominoTiles()
  {
    List<DominoTile> newDominoTiles = Collections.unmodifiableList(dominoTiles);
    return newDominoTiles;
  }

  public int numberOfDominoTiles()
  {
    int number = dominoTiles.size();
    return number;
  }

  public boolean hasDominoTiles()
  {
    boolean has = dominoTiles.size() > 0;
    return has;
  }

  public int indexOfDominoTile(DominoTile aDominoTile)
  {
    int index = dominoTiles.indexOf(aDominoTile);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setKingdom(Kingdom aKingdom)
  {
    boolean wasSet = false;
    if (aKingdom == null)
    {
      return wasSet;
    }

    Kingdom existingKingdom = kingdom;
    kingdom = aKingdom;
    if (existingKingdom != null && !existingKingdom.equals(aKingdom))
    {
      existingKingdom.removeRegion(this);
    }
    kingdom.addRegion(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfDominoTiles()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public DominoTile addDominoTile(int aXPosition, int aYPosition, ca.mcgill.ecse223.kingdomino.model.DominoTile.Landscape aLandscape, String aNumOfCrowns, boolean aIsReference, Domino aDomino, Kingdom aKingdom)
  {
    return new DominoTile(aXPosition, aYPosition, aLandscape, aNumOfCrowns, aIsReference, this, aDomino, aKingdom);
  }

  public boolean addDominoTile(DominoTile aDominoTile)
  {
    boolean wasAdded = false;
    if (dominoTiles.contains(aDominoTile)) { return false; }
    Region existingBelongsTo = aDominoTile.getBelongsTo();
    boolean isNewBelongsTo = existingBelongsTo != null && !this.equals(existingBelongsTo);
    if (isNewBelongsTo)
    {
      aDominoTile.setBelongsTo(this);
    }
    else
    {
      dominoTiles.add(aDominoTile);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDominoTile(DominoTile aDominoTile)
  {
    boolean wasRemoved = false;
    //Unable to remove aDominoTile, as it must always have a belongsTo
    if (!this.equals(aDominoTile.getBelongsTo()))
    {
      dominoTiles.remove(aDominoTile);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addDominoTileAt(DominoTile aDominoTile, int index)
  {  
    boolean wasAdded = false;
    if(addDominoTile(aDominoTile))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDominoTiles()) { index = numberOfDominoTiles() - 1; }
      dominoTiles.remove(aDominoTile);
      dominoTiles.add(index, aDominoTile);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDominoTileAt(DominoTile aDominoTile, int index)
  {
    boolean wasAdded = false;
    if(dominoTiles.contains(aDominoTile))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDominoTiles()) { index = numberOfDominoTiles() - 1; }
      dominoTiles.remove(aDominoTile);
      dominoTiles.add(index, aDominoTile);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addDominoTileAt(aDominoTile, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Kingdom placeholderKingdom = kingdom;
    this.kingdom = null;
    if(placeholderKingdom != null)
    {
      placeholderKingdom.removeRegion(this);
    }
    for(int i=dominoTiles.size(); i > 0; i--)
    {
      DominoTile aDominoTile = dominoTiles.get(i - 1);
      aDominoTile.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "size" + ":" + getSize()+ "," +
            "numCrowns" + ":" + getNumCrowns()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "landscape" + "=" + (getLandscape() != null ? !getLandscape().equals(this)  ? getLandscape().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "kingdom = "+(getKingdom()!=null?Integer.toHexString(System.identityHashCode(getKingdom())):"null");
  }
}