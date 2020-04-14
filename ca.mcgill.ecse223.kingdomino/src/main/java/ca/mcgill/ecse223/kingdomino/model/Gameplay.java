/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.kingdomino.model;

// line 3 "../../../../../Gameplay.ump"
public class Gameplay
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Gameplay State Machines
  public enum Gamestatus { Setup, Initializing, Playing }
  public enum GamestatusInitializing { Null, CreatingFirstDraft, SelectingFirstDomino }
  public enum GamestatusPlaying { Null, PlacingDomino, SelectingDomino }
  private Gamestatus gamestatus;
  private GamestatusInitializing gamestatusInitializing;
  private GamestatusPlaying gamestatusPlaying;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Gameplay()
  {
    setGamestatusInitializing(GamestatusInitializing.Null);
    setGamestatusPlaying(GamestatusPlaying.Null);
    setGamestatus(Gamestatus.Setup);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getGamestatusFullName()
  {
    String answer = gamestatus.toString();
    if (gamestatusInitializing != GamestatusInitializing.Null) { answer += "." + gamestatusInitializing.toString(); }
    if (gamestatusPlaying != GamestatusPlaying.Null) { answer += "." + gamestatusPlaying.toString(); }
    return answer;
  }

  public Gamestatus getGamestatus()
  {
    return gamestatus;
  }

  public GamestatusInitializing getGamestatusInitializing()
  {
    return gamestatusInitializing;
  }

  public GamestatusPlaying getGamestatusPlaying()
  {
    return gamestatusPlaying;
  }

  public boolean start()
  {
    boolean wasEventProcessed = false;
    
    Gamestatus aGamestatus = gamestatus;
    switch (aGamestatus)
    {
      case Setup:
        setGamestatusInitializing(GamestatusInitializing.CreatingFirstDraft);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean draftReady()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    switch (aGamestatusInitializing)
    {
      case CreatingFirstDraft:
        exitGamestatusInitializing();
        // line 12 "../../../../../Gameplay.ump"
        revealNextDraft(); setNextDraft(); generateInitialPlayerOrder();
        setGamestatusInitializing(GamestatusInitializing.SelectingFirstDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean firstSelectionComplete()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    switch (aGamestatusInitializing)
    {
      case SelectingFirstDomino:
        if (isSelectionValid())
        {
          exitGamestatusInitializing();
        // line 15 "../../../../../Gameplay.ump"
          nextPlayer();
          setGamestatusInitializing(GamestatusInitializing.SelectingFirstDomino);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean endOfTurn()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    switch (aGamestatusInitializing)
    {
      case SelectingFirstDomino:
        if (isCurrentPlayerTheLastInTurn()&&isSelectionValid())
        {
          exitGamestatus();
        // line 16 "../../../../../Gameplay.ump"
          setNextDraft(); generatePlayerOrder();
          setGamestatusPlaying(GamestatusPlaying.PlacingDomino);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean readyToPlace()
  {
    boolean wasEventProcessed = false;
    
    GamestatusPlaying aGamestatusPlaying = gamestatusPlaying;
    switch (aGamestatusPlaying)
    {
      case PlacingDomino:
        if (isDominoCorrectlyPreplaced())
        {
          exitGamestatusPlaying();
        // line 21 "../../../../../Gameplay.ump"
          placeDomino();
          setGamestatusPlaying(GamestatusPlaying.SelectingDomino);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void exitGamestatus()
  {
    switch(gamestatus)
    {
      case Initializing:
        exitGamestatusInitializing();
        break;
      case Playing:
        exitGamestatusPlaying();
        break;
    }
  }

  private void setGamestatus(Gamestatus aGamestatus)
  {
    gamestatus = aGamestatus;

    // entry actions and do activities
    switch(gamestatus)
    {
      case Initializing:
        if (gamestatusInitializing == GamestatusInitializing.Null) { setGamestatusInitializing(GamestatusInitializing.CreatingFirstDraft); }
        break;
      case Playing:
        if (gamestatusPlaying == GamestatusPlaying.Null) { setGamestatusPlaying(GamestatusPlaying.PlacingDomino); }
        break;
    }
  }

  private void exitGamestatusInitializing()
  {
    switch(gamestatusInitializing)
    {
      case CreatingFirstDraft:
        setGamestatusInitializing(GamestatusInitializing.Null);
        break;
      case SelectingFirstDomino:
        setGamestatusInitializing(GamestatusInitializing.Null);
        break;
    }
  }

  private void setGamestatusInitializing(GamestatusInitializing aGamestatusInitializing)
  {
    gamestatusInitializing = aGamestatusInitializing;
    if (gamestatus != Gamestatus.Initializing && aGamestatusInitializing != GamestatusInitializing.Null) { setGamestatus(Gamestatus.Initializing); }

    // entry actions and do activities
    switch(gamestatusInitializing)
    {
      case CreatingFirstDraft:
        // line 11 "../../../../../Gameplay.ump"
        shuffleDominos(); setNextDraft(); orderNextDraft();
        break;
    }
  }

  private void exitGamestatusPlaying()
  {
    switch(gamestatusPlaying)
    {
      case PlacingDomino:
        setGamestatusPlaying(GamestatusPlaying.Null);
        break;
      case SelectingDomino:
        setGamestatusPlaying(GamestatusPlaying.Null);
        break;
    }
  }

  private void setGamestatusPlaying(GamestatusPlaying aGamestatusPlaying)
  {
    gamestatusPlaying = aGamestatusPlaying;
    if (gamestatus != Gamestatus.Playing && aGamestatusPlaying != GamestatusPlaying.Null) { setGamestatus(Gamestatus.Playing); }
  }

  public void delete()
  {}


  /**
   * Setter for test setup
   */
  // line 33 "../../../../../Gameplay.ump"
   public void setGamestatus(String status){
    switch (status) {
       	case "CreatingFirstDraft":
       	    gamestatus = Gamestatus.Initializing;
       	    gamestatusInitializing = GamestatusInitializing.CreatingFirstDraft;
       	    gamestatusPlaying = GamestatusPlaying.Null;
       	    setGamestatusInitializing(GamestatusInitializing.CreatingFirstDraft);
       	    break;
       	case "SelectingFirstDomino":
       		gamestatus = Gamestatus.Initializing;
       	    gamestatusInitializing = GamestatusInitializing.SelectingFirstDomino;
       	    gamestatusPlaying = GamestatusPlaying.Null;
       	    setGamestatusInitializing(GamestatusInitializing.SelectingFirstDomino);
       	    break;
       	case "PlacingDomino":
       		gamestatus = Gamestatus.Playing;
       	    gamestatusInitializing = GamestatusInitializing.Null;
       	    gamestatusPlaying = GamestatusPlaying.PlacingDomino;
       	    setGamestatusPlaying(GamestatusPlaying.PlacingDomino);
       	    break;
       	default:
       	    throw new RuntimeException("Invalid gamestatus string was provided: " + status);
       	}
  }


  /**
   * Guards
   */
  // line 62 "../../../../../Gameplay.ump"
   public boolean isCurrentPlayerTheLastInTurn(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isCurrentPlayerTheLastInTurn();
  }

  // line 66 "../../../../../Gameplay.ump"
   public boolean isCurrentTurnTheLastInGame(){
    // TODO: implement this
        return false;
  }

  // line 71 "../../../../../Gameplay.ump"
   public boolean isSelectionValid(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isSelectionValid();
  }

  // line 75 "../../../../../Gameplay.ump"
   public boolean isDominoCorrectlyPreplaced(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isDominoCorrectlyPreplaced();
  }


  /**
   * You may need to add more guards here
   * Actions
   */
  // line 85 "../../../../../Gameplay.ump"
   public void shuffleDominos(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.shuffleDominos();
  }

  // line 89 "../../../../../Gameplay.ump"
   public void setNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.setNextDraft();
  }

  // line 93 "../../../../../Gameplay.ump"
   public void orderNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.orderNextDraft();
        draftReady();
  }

  // line 98 "../../../../../Gameplay.ump"
   public void revealNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.revealNextDraft();
  }

  // line 102 "../../../../../Gameplay.ump"
   public void generateInitialPlayerOrder(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.generateInitialPlayerOrder();
  }

  // line 106 "../../../../../Gameplay.ump"
   public void nextPlayer(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.nextPlayer();
  }

  // line 110 "../../../../../Gameplay.ump"
   public void generatePlayerOrder(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.generatePlayerOrder();
  }

  // line 114 "../../../../../Gameplay.ump"
   public void placeDomino(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.placeDomino();
  }

}