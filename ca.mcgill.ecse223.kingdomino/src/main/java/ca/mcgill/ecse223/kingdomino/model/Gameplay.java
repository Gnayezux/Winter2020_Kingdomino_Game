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
  public enum Gamestatus { Starting, Initializing }
  public enum GamestatusInitializing { Null, CreatingFirstDraft, SelectingFirstDomino, PlacingFirstDomino }
  private Gamestatus gamestatus;
  private GamestatusInitializing gamestatusInitializing;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Gameplay()
  {
    setGamestatusInitializing(GamestatusInitializing.Null);
    setGamestatus(Gamestatus.Starting);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getGamestatusFullName()
  {
    String answer = gamestatus.toString();
    if (gamestatusInitializing != GamestatusInitializing.Null) { answer += "." + gamestatusInitializing.toString(); }
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

  public boolean startGame()
  {
    boolean wasEventProcessed = false;
    
    Gamestatus aGamestatus = gamestatus;
    switch (aGamestatus)
    {
      case Starting:
        setGamestatus(Gamestatus.Initializing);
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
        revealNextDraft(); generateInitialPlayerOrder();
        setGamestatusInitializing(GamestatusInitializing.SelectingFirstDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean nextPlayerFirstSelection()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    switch (aGamestatusInitializing)
    {
      case SelectingFirstDomino:
        if (isValidSelection())
        {
          exitGamestatusInitializing();
        // line 15 "../../../../../Gameplay.ump"
          nextPlayerTurn();
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

  public boolean firstSelectionComplete()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    switch (aGamestatusInitializing)
    {
      case SelectingFirstDomino:
        if (isCurrentPlayerTheLastInTurn())
        {
          exitGamestatusInitializing();
          setGamestatusInitializing(GamestatusInitializing.PlacingFirstDomino);
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
        // line 17 "../../../../../Gameplay.ump"
        generatePlayerOrder();
        setGamestatusInitializing(GamestatusInitializing.Null);
        break;
      case PlacingFirstDomino:
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
        shuffleDominoPile(); createNextDraft(); orderNextDraft();
        break;
    }
  }

  public void delete()
  {}


  /**
   * Setter for test setup
   */
  // line 30 "../../../../../Gameplay.ump"
   public void setGamestatus(String status){
    switch (status) {
       	case "CreatingFirstDraft":
       	    gamestatus = Gamestatus.Initializing;
       	    gamestatusInitializing = GamestatusInitializing.CreatingFirstDraft;
       	    setGamestatusInitializing(GamestatusInitializing.CreatingFirstDraft);
       	    break;
       	case "SelectingFirstDomino":
       	// TODO add further cases here to set desired state
       	default:
       	    throw new RuntimeException("Invalid gamestatus string was provided: " + status);
       	}
  }


  /**
   * Guards
   */
  // line 48 "../../../../../Gameplay.ump"
   public boolean isCurrentPlayerTheLastInTurn(){
    // TODO: implement this
        return false;
  }

  // line 53 "../../../../../Gameplay.ump"
   public boolean isCurrentTurnTheLastInGame(){
    // TODO: implement this
        return false;
  }

  // line 58 "../../../../../Gameplay.ump"
   public boolean isValidSelection(){
    // TODO: implement this
        return false;
  }


  /**
   * You may need to add more guards here
   * Actions
   */
  // line 69 "../../../../../Gameplay.ump"
   public void shuffleDominoPile(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.doAction("ShuffleDominoPile");
  }

  // line 73 "../../../../../Gameplay.ump"
   public void createNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.doAction("CreateNextDraft");
  }

  // line 77 "../../../../../Gameplay.ump"
   public void orderNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.doAction("OrderNextDraft");
  }

  // line 81 "../../../../../Gameplay.ump"
   public void revealNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.doAction("RevealNextDraft");
  }

  // line 85 "../../../../../Gameplay.ump"
   public void generateInitialPlayerOrder(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.doAction("GenerateInitialPlayerOrder");
  }

  // line 89 "../../../../../Gameplay.ump"
   public void nextPlayerTurn(){
    
  }

  // line 92 "../../../../../Gameplay.ump"
   public void generatePlayerOrder(){
    
  }

}