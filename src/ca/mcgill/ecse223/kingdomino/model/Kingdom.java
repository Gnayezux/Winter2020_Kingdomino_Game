package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 37 "Kingdomino_i1.ump"
public class Kingdom
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Landscape { Forest, Meadows, Water, Deserts, Mines, Wastelands }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Kingdom Attributes
  private int totalScore;
  private int currentWidth;
  private int currentHeight;
  private int maxHeight;
  private int maxWidth;

  //Kingdom Associations
  private List<Region> regions;
  private List<DominoTile> tilesInTerritory;
  private Player player;
  private CastleTile castle;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Kingdom(int aTotalScore, int aCurrentWidth, int aCurrentHeight, int aMaxHeight, int aMaxWidth, Player aPlayer, CastleTile aCastle)
  {
    totalScore = aTotalScore;
    currentWidth = aCurrentWidth;
    currentHeight = aCurrentHeight;
    maxHeight = aMaxHeight;
    maxWidth = aMaxWidth;
    regions = new ArrayList<Region>();
    tilesInTerritory = new ArrayList<DominoTile>();
    if (aPlayer == null || aPlayer.getPersonalKingdom() != null)
    {
      throw new RuntimeException("Unable to create Kingdom due to aPlayer");
    }
    player = aPlayer;
    if (aCastle == null || aCastle.getKingdom() != null)
    {
      throw new RuntimeException("Unable to create Kingdom due to aCastle");
    }
    castle = aCastle;
  }

  public Kingdom(int aTotalScore, int aCurrentWidth, int aCurrentHeight, int aMaxHeight, int aMaxWidth, Profile aSelectedProfileForPlayer, Game aGameForPlayer, int aXPositionForCastle, int aYPositionForCastle)
  {
    totalScore = aTotalScore;
    currentWidth = aCurrentWidth;
    currentHeight = aCurrentHeight;
    maxHeight = aMaxHeight;
    maxWidth = aMaxWidth;
    regions = new ArrayList<Region>();
    tilesInTerritory = new ArrayList<DominoTile>();
    player = new Player(aSelectedProfileForPlayer, this, aGameForPlayer);
    castle = new CastleTile(aXPositionForCastle, aYPositionForCastle, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTotalScore(int aTotalScore)
  {
    boolean wasSet = false;
    totalScore = aTotalScore;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentWidth(int aCurrentWidth)
  {
    boolean wasSet = false;
    currentWidth = aCurrentWidth;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentHeight(int aCurrentHeight)
  {
    boolean wasSet = false;
    currentHeight = aCurrentHeight;
    wasSet = true;
    return wasSet;
  }

  public boolean setMaxHeight(int aMaxHeight)
  {
    boolean wasSet = false;
    maxHeight = aMaxHeight;
    wasSet = true;
    return wasSet;
  }

  public boolean setMaxWidth(int aMaxWidth)
  {
    boolean wasSet = false;
    maxWidth = aMaxWidth;
    wasSet = true;
    return wasSet;
  }

  public int getTotalScore()
  {
    return totalScore;
  }

  public int getCurrentWidth()
  {
    return currentWidth;
  }

  public int getCurrentHeight()
  {
    return currentHeight;
  }

  public int getMaxHeight()
  {
    return maxHeight;
  }

  public int getMaxWidth()
  {
    return maxWidth;
  }
  /* Code from template association_GetMany */
  public Region getRegion(int index)
  {
    Region aRegion = regions.get(index);
    return aRegion;
  }

  public List<Region> getRegions()
  {
    List<Region> newRegions = Collections.unmodifiableList(regions);
    return newRegions;
  }

  public int numberOfRegions()
  {
    int number = regions.size();
    return number;
  }

  public boolean hasRegions()
  {
    boolean has = regions.size() > 0;
    return has;
  }

  public int indexOfRegion(Region aRegion)
  {
    int index = regions.indexOf(aRegion);
    return index;
  }
  /* Code from template association_GetMany */
  public DominoTile getTilesInTerritory(int index)
  {
    DominoTile aTilesInTerritory = tilesInTerritory.get(index);
    return aTilesInTerritory;
  }

  public List<DominoTile> getTilesInTerritory()
  {
    List<DominoTile> newTilesInTerritory = Collections.unmodifiableList(tilesInTerritory);
    return newTilesInTerritory;
  }

  public int numberOfTilesInTerritory()
  {
    int number = tilesInTerritory.size();
    return number;
  }

  public boolean hasTilesInTerritory()
  {
    boolean has = tilesInTerritory.size() > 0;
    return has;
  }

  public int indexOfTilesInTerritory(DominoTile aTilesInTerritory)
  {
    int index = tilesInTerritory.indexOf(aTilesInTerritory);
    return index;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }
  /* Code from template association_GetOne */
  public CastleTile getCastle()
  {
    return castle;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRegions()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Region addRegion(ca.mcgill.ecse223.kingdomino.model.Region.Landscape aLandscape, int aSize, int aNumCrowns)
  {
    return new Region(aLandscape, aSize, aNumCrowns, this);
  }

  public boolean addRegion(Region aRegion)
  {
    boolean wasAdded = false;
    if (regions.contains(aRegion)) { return false; }
    Kingdom existingKingdom = aRegion.getKingdom();
    boolean isNewKingdom = existingKingdom != null && !this.equals(existingKingdom);
    if (isNewKingdom)
    {
      aRegion.setKingdom(this);
    }
    else
    {
      regions.add(aRegion);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRegion(Region aRegion)
  {
    boolean wasRemoved = false;
    //Unable to remove aRegion, as it must always have a kingdom
    if (!this.equals(aRegion.getKingdom()))
    {
      regions.remove(aRegion);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRegionAt(Region aRegion, int index)
  {  
    boolean wasAdded = false;
    if(addRegion(aRegion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRegions()) { index = numberOfRegions() - 1; }
      regions.remove(aRegion);
      regions.add(index, aRegion);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRegionAt(Region aRegion, int index)
  {
    boolean wasAdded = false;
    if(regions.contains(aRegion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRegions()) { index = numberOfRegions() - 1; }
      regions.remove(aRegion);
      regions.add(index, aRegion);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRegionAt(aRegion, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTilesInTerritory()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfTilesInTerritory()
  {
    return 24;
  }
  /* Code from template association_AddOptionalNToOne */
  public DominoTile addTilesInTerritory(int aXPosition, int aYPosition, ca.mcgill.ecse223.kingdomino.model.DominoTile.Landscape aLandscape, String aNumOfCrowns, boolean aIsReference, Region aBelongsTo, Domino aDomino)
  {
    if (numberOfTilesInTerritory() >= maximumNumberOfTilesInTerritory())
    {
      return null;
    }
    else
    {
      return new DominoTile(aXPosition, aYPosition, aLandscape, aNumOfCrowns, aIsReference, aBelongsTo, aDomino, this);
    }
  }

  public boolean addTilesInTerritory(DominoTile aTilesInTerritory)
  {
    boolean wasAdded = false;
    if (tilesInTerritory.contains(aTilesInTerritory)) { return false; }
    if (numberOfTilesInTerritory() >= maximumNumberOfTilesInTerritory())
    {
      return wasAdded;
    }

    Kingdom existingKingdom = aTilesInTerritory.getKingdom();
    boolean isNewKingdom = existingKingdom != null && !this.equals(existingKingdom);
    if (isNewKingdom)
    {
      aTilesInTerritory.setKingdom(this);
    }
    else
    {
      tilesInTerritory.add(aTilesInTerritory);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTilesInTerritory(DominoTile aTilesInTerritory)
  {
    boolean wasRemoved = false;
    //Unable to remove aTilesInTerritory, as it must always have a kingdom
    if (!this.equals(aTilesInTerritory.getKingdom()))
    {
      tilesInTerritory.remove(aTilesInTerritory);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTilesInTerritoryAt(DominoTile aTilesInTerritory, int index)
  {  
    boolean wasAdded = false;
    if(addTilesInTerritory(aTilesInTerritory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTilesInTerritory()) { index = numberOfTilesInTerritory() - 1; }
      tilesInTerritory.remove(aTilesInTerritory);
      tilesInTerritory.add(index, aTilesInTerritory);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTilesInTerritoryAt(DominoTile aTilesInTerritory, int index)
  {
    boolean wasAdded = false;
    if(tilesInTerritory.contains(aTilesInTerritory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTilesInTerritory()) { index = numberOfTilesInTerritory() - 1; }
      tilesInTerritory.remove(aTilesInTerritory);
      tilesInTerritory.add(index, aTilesInTerritory);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTilesInTerritoryAt(aTilesInTerritory, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (regions.size() > 0)
    {
      Region aRegion = regions.get(regions.size() - 1);
      aRegion.delete();
      regions.remove(aRegion);
    }
    
    for(int i=tilesInTerritory.size(); i > 0; i--)
    {
      DominoTile aTilesInTerritory = tilesInTerritory.get(i - 1);
      aTilesInTerritory.delete();
    }
    Player existingPlayer = player;
    player = null;
    if (existingPlayer != null)
    {
      existingPlayer.delete();
    }
    CastleTile existingCastle = castle;
    castle = null;
    if (existingCastle != null)
    {
      existingCastle.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "totalScore" + ":" + getTotalScore()+ "," +
            "currentWidth" + ":" + getCurrentWidth()+ "," +
            "currentHeight" + ":" + getCurrentHeight()+ "," +
            "maxHeight" + ":" + getMaxHeight()+ "," +
            "maxWidth" + ":" + getMaxWidth()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "castle = "+(getCastle()!=null?Integer.toHexString(System.identityHashCode(getCastle())):"null");
  }
}