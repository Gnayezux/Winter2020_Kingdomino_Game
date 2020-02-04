/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 73 "../A.ump"
// line 119 "../A.ump"
public class castleBlock
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //castleBlock Attributes
  private player player_;

  //castleBlock Associations
  private player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public castleBlock(player aPlayer_, player aPlayer)
  {
    player_ = aPlayer_;
    if (aPlayer == null || aPlayer.getCastleBlock() != null)
    {
      throw new RuntimeException("Unable to create castleBlock due to aPlayer");
    }
    player = aPlayer;
  }

  public castleBlock(player aPlayer_, int aScoreForPlayer, String aKingColorForPlayer, playArea aPlayAreaForPlayer)
  {
    player_ = aPlayer_;
    player = new player(aScoreForPlayer, aKingColorForPlayer, aPlayAreaForPlayer, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPlayer_(player aPlayer_)
  {
    boolean wasSet = false;
    player_ = aPlayer_;
    wasSet = true;
    return wasSet;
  }

  public player getPlayer_()
  {
    return player_;
  }
  /* Code from template association_GetOne */
  public player getPlayer()
  {
    return player;
  }

  public void delete()
  {
    player existingPlayer = player;
    player = null;
    if (existingPlayer != null)
    {
      existingPlayer.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player_" + "=" + (getPlayer_() != null ? !getPlayer_().equals(this)  ? getPlayer_().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null");
  }
}