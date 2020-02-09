package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/



// line 85 "Kingdomino_i1.ump"
public abstract class Tile
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Tile Attributes
  private int xPosition;
  private int yPosition;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Tile(int aXPosition, int aYPosition)
  {
    xPosition = aXPosition;
    yPosition = aYPosition;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setXPosition(int aXPosition)
  {
    boolean wasSet = false;
    xPosition = aXPosition;
    wasSet = true;
    return wasSet;
  }

  public boolean setYPosition(int aYPosition)
  {
    boolean wasSet = false;
    yPosition = aYPosition;
    wasSet = true;
    return wasSet;
  }

  public int getXPosition()
  {
    return xPosition;
  }

  public int getYPosition()
  {
    return yPosition;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "xPosition" + ":" + getXPosition()+ "," +
            "yPosition" + ":" + getYPosition()+ "]";
  }
}