package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/



// line 17 "Kingdomino_i1.ump"
public class Profile
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Profile Attributes
  private String username;
  private int wins;
  private int gamesPlayed;
  private int losses;
  private int highscore;
  private int totalPoints;
  private int ties;

  //Profile Associations
  private Kingdomino kingdomino;
  private Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Profile(String aUsername, int aWins, int aGamesPlayed, int aLosses, int aHighscore, int aTotalPoints, int aTies, Kingdomino aKingdomino, Player aPlayer)
  {
    username = aUsername;
    wins = aWins;
    gamesPlayed = aGamesPlayed;
    losses = aLosses;
    highscore = aHighscore;
    totalPoints = aTotalPoints;
    ties = aTies;
    boolean didAddKingdomino = setKingdomino(aKingdomino);
    if (!didAddKingdomino)
    {
      throw new RuntimeException("Unable to create profile due to kingdomino");
    }
    if (aPlayer == null || aPlayer.getSelectedProfile() != null)
    {
      throw new RuntimeException("Unable to create Profile due to aPlayer");
    }
    player = aPlayer;
  }

  public Profile(String aUsername, int aWins, int aGamesPlayed, int aLosses, int aHighscore, int aTotalPoints, int aTies, Kingdomino aKingdomino, Kingdom aPersonalKingdomForPlayer, Game aGameForPlayer)
  {
    username = aUsername;
    wins = aWins;
    gamesPlayed = aGamesPlayed;
    losses = aLosses;
    highscore = aHighscore;
    totalPoints = aTotalPoints;
    ties = aTies;
    boolean didAddKingdomino = setKingdomino(aKingdomino);
    if (!didAddKingdomino)
    {
      throw new RuntimeException("Unable to create profile due to kingdomino");
    }
    player = new Player(this, aPersonalKingdomForPlayer, aGameForPlayer);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUsername(String aUsername)
  {
    boolean wasSet = false;
    username = aUsername;
    wasSet = true;
    return wasSet;
  }

  public boolean setWins(int aWins)
  {
    boolean wasSet = false;
    wins = aWins;
    wasSet = true;
    return wasSet;
  }

  public boolean setGamesPlayed(int aGamesPlayed)
  {
    boolean wasSet = false;
    gamesPlayed = aGamesPlayed;
    wasSet = true;
    return wasSet;
  }

  public boolean setLosses(int aLosses)
  {
    boolean wasSet = false;
    losses = aLosses;
    wasSet = true;
    return wasSet;
  }

  public boolean setHighscore(int aHighscore)
  {
    boolean wasSet = false;
    highscore = aHighscore;
    wasSet = true;
    return wasSet;
  }

  public boolean setTotalPoints(int aTotalPoints)
  {
    boolean wasSet = false;
    totalPoints = aTotalPoints;
    wasSet = true;
    return wasSet;
  }

  public boolean setTies(int aTies)
  {
    boolean wasSet = false;
    ties = aTies;
    wasSet = true;
    return wasSet;
  }

  public String getUsername()
  {
    return username;
  }

  public int getWins()
  {
    return wins;
  }

  public int getGamesPlayed()
  {
    return gamesPlayed;
  }

  public int getLosses()
  {
    return losses;
  }

  public int getHighscore()
  {
    return highscore;
  }

  public int getTotalPoints()
  {
    return totalPoints;
  }

  public int getTies()
  {
    return ties;
  }
  /* Code from template association_GetOne */
  public Kingdomino getKingdomino()
  {
    return kingdomino;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }
  /* Code from template association_SetOneToMany */
  public boolean setKingdomino(Kingdomino aKingdomino)
  {
    boolean wasSet = false;
    if (aKingdomino == null)
    {
      return wasSet;
    }

    Kingdomino existingKingdomino = kingdomino;
    kingdomino = aKingdomino;
    if (existingKingdomino != null && !existingKingdomino.equals(aKingdomino))
    {
      existingKingdomino.removeProfile(this);
    }
    kingdomino.addProfile(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Kingdomino placeholderKingdomino = kingdomino;
    this.kingdomino = null;
    if(placeholderKingdomino != null)
    {
      placeholderKingdomino.removeProfile(this);
    }
    Player existingPlayer = player;
    player = null;
    if (existingPlayer != null)
    {
      existingPlayer.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "username" + ":" + getUsername()+ "," +
            "wins" + ":" + getWins()+ "," +
            "gamesPlayed" + ":" + getGamesPlayed()+ "," +
            "losses" + ":" + getLosses()+ "," +
            "highscore" + ":" + getHighscore()+ "," +
            "totalPoints" + ":" + getTotalPoints()+ "," +
            "ties" + ":" + getTies()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "kingdomino = "+(getKingdomino()!=null?Integer.toHexString(System.identityHashCode(getKingdomino())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null");
  }
}