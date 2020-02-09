package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/



// line 54 "Kingdomino_i1.ump"
public class CastleTile extends Tile
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CastleTile Associations
  private Kingdom kingdom;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CastleTile(int aXPosition, int aYPosition, Kingdom aKingdom)
  {
    super(aXPosition, aYPosition);
    if (aKingdom == null || aKingdom.getCastle() != null)
    {
      throw new RuntimeException("Unable to create CastleTile due to aKingdom");
    }
    kingdom = aKingdom;
  }

  public CastleTile(int aXPosition, int aYPosition, int aTotalScoreForKingdom, int aCurrentWidthForKingdom, int aCurrentHeightForKingdom, int aMaxHeightForKingdom, int aMaxWidthForKingdom, Player aPlayerForKingdom)
  {
    super(aXPosition, aYPosition);
    kingdom = new Kingdom(aTotalScoreForKingdom, aCurrentWidthForKingdom, aCurrentHeightForKingdom, aMaxHeightForKingdom, aMaxWidthForKingdom, aPlayerForKingdom, this);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Kingdom getKingdom()
  {
    return kingdom;
  }

  public void delete()
  {
    Kingdom existingKingdom = kingdom;
    kingdom = null;
    if (existingKingdom != null)
    {
      existingKingdom.delete();
    }
    super.delete();
  }

}