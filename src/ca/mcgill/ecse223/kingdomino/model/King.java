package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/



// line 90 "Kingdomino_i1.ump"
public class King
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Color { Blue, Red, Green, Yellow }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //King Attributes
  private Color color;
  private Domino dominoSelected;

  //King Associations
  private Player player;
  private Draft draft;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public King(Color aColor, Domino aDominoSelected, Player aPlayer, Draft aDraft)
  {
    color = aColor;
    dominoSelected = aDominoSelected;
    boolean didAddPlayer = setPlayer(aPlayer);
    if (!didAddPlayer)
    {
      throw new RuntimeException("Unable to create king due to player");
    }
    boolean didAddDraft = setDraft(aDraft);
    if (!didAddDraft)
    {
      throw new RuntimeException("Unable to create king due to draft");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setColor(Color aColor)
  {
    boolean wasSet = false;
    color = aColor;
    wasSet = true;
    return wasSet;
  }

  public boolean setDominoSelected(Domino aDominoSelected)
  {
    boolean wasSet = false;
    dominoSelected = aDominoSelected;
    wasSet = true;
    return wasSet;
  }

  public Color getColor()
  {
    return color;
  }

  public Domino getDominoSelected()
  {
    return dominoSelected;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }
  /* Code from template association_GetOne */
  public Draft getDraft()
  {
    return draft;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setPlayer(Player aPlayer)
  {
    boolean wasSet = false;
    //Must provide player to king
    if (aPlayer == null)
    {
      return wasSet;
    }

    //player already at maximum (2)
    if (aPlayer.numberOfKings() >= Player.maximumNumberOfKings())
    {
      return wasSet;
    }
    
    Player existingPlayer = player;
    player = aPlayer;
    if (existingPlayer != null && !existingPlayer.equals(aPlayer))
    {
      boolean didRemove = existingPlayer.removeKing(this);
      if (!didRemove)
      {
        player = existingPlayer;
        return wasSet;
      }
    }
    player.addKing(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setDraft(Draft aDraft)
  {
    boolean wasSet = false;
    //Must provide draft to king
    if (aDraft == null)
    {
      return wasSet;
    }

    //draft already at maximum (4)
    if (aDraft.numberOfKings() >= Draft.maximumNumberOfKings())
    {
      return wasSet;
    }
    
    Draft existingDraft = draft;
    draft = aDraft;
    if (existingDraft != null && !existingDraft.equals(aDraft))
    {
      boolean didRemove = existingDraft.removeKing(this);
      if (!didRemove)
      {
        draft = existingDraft;
        return wasSet;
      }
    }
    draft.addKing(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Player placeholderPlayer = player;
    this.player = null;
    if(placeholderPlayer != null)
    {
      placeholderPlayer.removeKing(this);
    }
    Draft placeholderDraft = draft;
    this.draft = null;
    if(placeholderDraft != null)
    {
      placeholderDraft.removeKing(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "color" + "=" + (getColor() != null ? !getColor().equals(this)  ? getColor().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "dominoSelected" + "=" + (getDominoSelected() != null ? !getDominoSelected().equals(this)  ? getDominoSelected().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "draft = "+(getDraft()!=null?Integer.toHexString(System.identityHashCode(getDraft())):"null");
  }
}