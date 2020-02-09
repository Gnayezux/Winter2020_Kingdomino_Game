package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/



// line 59 "Kingdomino_i1.ump"
public class DominoTile extends Tile
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Landscape { Forest, Meadows, Water, Deserts, Mines, Wastelands }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //DominoTile Attributes
  private Landscape landscape;
  private String numOfCrowns;
  private boolean isReference;

  //DominoTile Associations
  private Region belongsTo;
  private Domino domino;
  private Kingdom kingdom;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public DominoTile(int aXPosition, int aYPosition, Landscape aLandscape, String aNumOfCrowns, boolean aIsReference, Region aBelongsTo, Domino aDomino, Kingdom aKingdom)
  {
    super(aXPosition, aYPosition);
    landscape = aLandscape;
    numOfCrowns = aNumOfCrowns;
    isReference = aIsReference;
    boolean didAddBelongsTo = setBelongsTo(aBelongsTo);
    if (!didAddBelongsTo)
    {
      throw new RuntimeException("Unable to create dominoTile due to belongsTo");
    }
    boolean didAddDomino = setDomino(aDomino);
    if (!didAddDomino)
    {
      throw new RuntimeException("Unable to create tilesOnDomino due to domino");
    }
    boolean didAddKingdom = setKingdom(aKingdom);
    if (!didAddKingdom)
    {
      throw new RuntimeException("Unable to create tilesInTerritory due to kingdom");
    }
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

  public boolean setNumOfCrowns(String aNumOfCrowns)
  {
    boolean wasSet = false;
    numOfCrowns = aNumOfCrowns;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsReference(boolean aIsReference)
  {
    boolean wasSet = false;
    isReference = aIsReference;
    wasSet = true;
    return wasSet;
  }

  public Landscape getLandscape()
  {
    return landscape;
  }

  public String getNumOfCrowns()
  {
    return numOfCrowns;
  }

  public boolean getIsReference()
  {
    return isReference;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsReference()
  {
    return isReference;
  }
  /* Code from template association_GetOne */
  public Region getBelongsTo()
  {
    return belongsTo;
  }
  /* Code from template association_GetOne */
  public Domino getDomino()
  {
    return domino;
  }
  /* Code from template association_GetOne */
  public Kingdom getKingdom()
  {
    return kingdom;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBelongsTo(Region aBelongsTo)
  {
    boolean wasSet = false;
    if (aBelongsTo == null)
    {
      return wasSet;
    }

    Region existingBelongsTo = belongsTo;
    belongsTo = aBelongsTo;
    if (existingBelongsTo != null && !existingBelongsTo.equals(aBelongsTo))
    {
      existingBelongsTo.removeDominoTile(this);
    }
    belongsTo.addDominoTile(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setDomino(Domino aDomino)
  {
    boolean wasSet = false;
    //Must provide domino to tilesOnDomino
    if (aDomino == null)
    {
      return wasSet;
    }

    //domino already at maximum (2)
    if (aDomino.numberOfTilesOnDomino() >= Domino.maximumNumberOfTilesOnDomino())
    {
      return wasSet;
    }
    
    Domino existingDomino = domino;
    domino = aDomino;
    if (existingDomino != null && !existingDomino.equals(aDomino))
    {
      boolean didRemove = existingDomino.removeTilesOnDomino(this);
      if (!didRemove)
      {
        domino = existingDomino;
        return wasSet;
      }
    }
    domino.addTilesOnDomino(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setKingdom(Kingdom aKingdom)
  {
    boolean wasSet = false;
    //Must provide kingdom to tilesInTerritory
    if (aKingdom == null)
    {
      return wasSet;
    }

    //kingdom already at maximum (24)
    if (aKingdom.numberOfTilesInTerritory() >= Kingdom.maximumNumberOfTilesInTerritory())
    {
      return wasSet;
    }
    
    Kingdom existingKingdom = kingdom;
    kingdom = aKingdom;
    if (existingKingdom != null && !existingKingdom.equals(aKingdom))
    {
      boolean didRemove = existingKingdom.removeTilesInTerritory(this);
      if (!didRemove)
      {
        kingdom = existingKingdom;
        return wasSet;
      }
    }
    kingdom.addTilesInTerritory(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Region placeholderBelongsTo = belongsTo;
    this.belongsTo = null;
    if(placeholderBelongsTo != null)
    {
      placeholderBelongsTo.removeDominoTile(this);
    }
    Domino placeholderDomino = domino;
    this.domino = null;
    if(placeholderDomino != null)
    {
      placeholderDomino.removeTilesOnDomino(this);
    }
    Kingdom placeholderKingdom = kingdom;
    this.kingdom = null;
    if(placeholderKingdom != null)
    {
      placeholderKingdom.removeTilesInTerritory(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "numOfCrowns" + ":" + getNumOfCrowns()+ "," +
            "isReference" + ":" + getIsReference()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "landscape" + "=" + (getLandscape() != null ? !getLandscape().equals(this)  ? getLandscape().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "belongsTo = "+(getBelongsTo()!=null?Integer.toHexString(System.identityHashCode(getBelongsTo())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "domino = "+(getDomino()!=null?Integer.toHexString(System.identityHashCode(getDomino())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "kingdom = "+(getKingdom()!=null?Integer.toHexString(System.identityHashCode(getKingdom())):"null");
  }
}