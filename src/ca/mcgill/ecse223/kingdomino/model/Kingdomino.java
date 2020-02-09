package ca.mcgill.ecse223.kingdomino.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 1 "Kingdomino_i1.ump"
public class Kingdomino
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Kingdomino Associations
  private Game currentGame;
  private List<Profile> profiles;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Kingdomino(Game aCurrentGame)
  {
    if (aCurrentGame == null || aCurrentGame.getKingdomino() != null)
    {
      throw new RuntimeException("Unable to create Kingdomino due to aCurrentGame");
    }
    currentGame = aCurrentGame;
    profiles = new ArrayList<Profile>();
  }

  public Kingdomino(int aNumPlayersForCurrentGame, int aNumTurnsForCurrentGame, Deck aDeckForCurrentGame)
  {
    currentGame = new Game(aNumPlayersForCurrentGame, aNumTurnsForCurrentGame, this, aDeckForCurrentGame);
    profiles = new ArrayList<Profile>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Game getCurrentGame()
  {
    return currentGame;
  }
  /* Code from template association_GetMany */
  public Profile getProfile(int index)
  {
    Profile aProfile = profiles.get(index);
    return aProfile;
  }

  public List<Profile> getProfiles()
  {
    List<Profile> newProfiles = Collections.unmodifiableList(profiles);
    return newProfiles;
  }

  public int numberOfProfiles()
  {
    int number = profiles.size();
    return number;
  }

  public boolean hasProfiles()
  {
    boolean has = profiles.size() > 0;
    return has;
  }

  public int indexOfProfile(Profile aProfile)
  {
    int index = profiles.indexOf(aProfile);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfProfiles()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Profile addProfile(String aUsername, int aWins, int aGamesPlayed, int aLosses, int aHighscore, int aTotalPoints, int aTies, Player aPlayer)
  {
    return new Profile(aUsername, aWins, aGamesPlayed, aLosses, aHighscore, aTotalPoints, aTies, this, aPlayer);
  }

  public boolean addProfile(Profile aProfile)
  {
    boolean wasAdded = false;
    if (profiles.contains(aProfile)) { return false; }
    Kingdomino existingKingdomino = aProfile.getKingdomino();
    boolean isNewKingdomino = existingKingdomino != null && !this.equals(existingKingdomino);
    if (isNewKingdomino)
    {
      aProfile.setKingdomino(this);
    }
    else
    {
      profiles.add(aProfile);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeProfile(Profile aProfile)
  {
    boolean wasRemoved = false;
    //Unable to remove aProfile, as it must always have a kingdomino
    if (!this.equals(aProfile.getKingdomino()))
    {
      profiles.remove(aProfile);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addProfileAt(Profile aProfile, int index)
  {  
    boolean wasAdded = false;
    if(addProfile(aProfile))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfProfiles()) { index = numberOfProfiles() - 1; }
      profiles.remove(aProfile);
      profiles.add(index, aProfile);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveProfileAt(Profile aProfile, int index)
  {
    boolean wasAdded = false;
    if(profiles.contains(aProfile))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfProfiles()) { index = numberOfProfiles() - 1; }
      profiles.remove(aProfile);
      profiles.add(index, aProfile);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addProfileAt(aProfile, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Game existingCurrentGame = currentGame;
    currentGame = null;
    if (existingCurrentGame != null)
    {
      existingCurrentGame.delete();
    }
    while (profiles.size() > 0)
    {
      Profile aProfile = profiles.get(profiles.size() - 1);
      aProfile.delete();
      profiles.remove(aProfile);
    }
    
  }

}