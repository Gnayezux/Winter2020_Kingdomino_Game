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
  public enum GamestatusPlaying { Null, PlacingDomino, SelectingDomino, CreatingDraft, EndingGame }
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
    GamestatusPlaying aGamestatusPlaying = gamestatusPlaying;
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

    switch (aGamestatusPlaying)
    {
      case CreatingDraft:
        exitGamestatusPlaying();
        // line 33 "../../../../../Gameplay.ump"
        generatePlayerOrder();
        setGamestatusPlaying(GamestatusPlaying.PlacingDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean selectionComplete()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    GamestatusPlaying aGamestatusPlaying = gamestatusPlaying;
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

    switch (aGamestatusPlaying)
    {
      case SelectingDomino:
        if (isSelectionValid())
        {
          exitGamestatusPlaying();
        // line 28 "../../../../../Gameplay.ump"
          nextPlayer();
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

  public boolean endOfTurn()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    GamestatusPlaying aGamestatusPlaying = gamestatusPlaying;
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

    switch (aGamestatusPlaying)
    {
      case SelectingDomino:
        if (isCurrentPlayerTheLastInTurn()&&isSelectionValid())
        {
          exitGamestatusPlaying();
        // line 29 "../../../../../Gameplay.ump"
          setNextDraft(); generatePlayerOrder();
          setGamestatusPlaying(GamestatusPlaying.CreatingDraft);
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
        if (isDominoCorrectlyPreplaced()&&!(isCurrentTurnTheLastInGame()))
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

  public boolean discard()
  {
    boolean wasEventProcessed = false;
    
    GamestatusPlaying aGamestatusPlaying = gamestatusPlaying;
    switch (aGamestatusPlaying)
    {
      case PlacingDomino:
        if (isImpossibleToPlace()&&!(isCurrentTurnTheLastInGame()))
        {
          exitGamestatusPlaying();
        // line 22 "../../../../../Gameplay.ump"
          discardDomino();
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

  public boolean placeLast()
  {
    boolean wasEventProcessed = false;
    
    GamestatusPlaying aGamestatusPlaying = gamestatusPlaying;
    switch (aGamestatusPlaying)
    {
      case PlacingDomino:
        if (isDominoCorrectlyPreplaced()&&isCurrentTurnTheLastInGame())
        {
          exitGamestatusPlaying();
        // line 23 "../../../../../Gameplay.ump"
          placeDomino(); nextPlayer();
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

  public boolean discardLast()
  {
    boolean wasEventProcessed = false;
    
    GamestatusPlaying aGamestatusPlaying = gamestatusPlaying;
    switch (aGamestatusPlaying)
    {
      case PlacingDomino:
        if (isImpossibleToPlace()&&isCurrentTurnTheLastInGame())
        {
          exitGamestatusPlaying();
        // line 24 "../../../../../Gameplay.ump"
          discardDomino(); nextPlayer();
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

  public boolean endGame()
  {
    boolean wasEventProcessed = false;
    
    GamestatusPlaying aGamestatusPlaying = gamestatusPlaying;
    switch (aGamestatusPlaying)
    {
      case PlacingDomino:
        if (isCurrentTurnTheLastInGame()&&isCurrentPlayerTheLastInTurn())
        {
          exitGamestatusPlaying();
          setGamestatusPlaying(GamestatusPlaying.EndingGame);
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
      case CreatingDraft:
        setGamestatusPlaying(GamestatusPlaying.Null);
        break;
      case EndingGame:
        setGamestatusPlaying(GamestatusPlaying.Null);
        break;
    }
  }

  private void setGamestatusPlaying(GamestatusPlaying aGamestatusPlaying)
  {
    gamestatusPlaying = aGamestatusPlaying;
    if (gamestatus != Gamestatus.Playing && aGamestatusPlaying != GamestatusPlaying.Null) { setGamestatus(Gamestatus.Playing); }

    // entry actions and do activities
    switch(gamestatusPlaying)
    {
      case CreatingDraft:
        // line 32 "../../../../../Gameplay.ump"
        setNextDraft(); orderNextDraft(); revealNextDraft();
        break;
      case EndingGame:
        // line 36 "../../../../../Gameplay.ump"
        calculatingScores();
        break;
    }
  }

  public void delete()
  {}


  /**
   * Setter for test setup
   */
  // line 46 "../../../../../Gameplay.ump"
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
       	case "SelectingDomino":
       		gamestatus = Gamestatus.Playing;
       	    gamestatusInitializing = GamestatusInitializing.Null;
       	    gamestatusPlaying = GamestatusPlaying.SelectingDomino;
       	    setGamestatusPlaying(GamestatusPlaying.SelectingDomino);
       	    break;
       	case "CreatingDraft":
       		gamestatus = Gamestatus.Playing;
       	    gamestatusInitializing = GamestatusInitializing.Null;
       	    gamestatusPlaying = GamestatusPlaying.CreatingDraft;
       	    setGamestatusPlaying(GamestatusPlaying.CreatingDraft);
       	    break;       	    
       	default:
       	    throw new RuntimeException("Invalid gamestatus string was provided: " + status);
       	}
  }


  /**
   * Guards
   */
  // line 87 "../../../../../Gameplay.ump"
   public boolean isCurrentPlayerTheLastInTurn(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isCurrentPlayerTheLastInTurn();
  }

  // line 91 "../../../../../Gameplay.ump"
   public boolean isCurrentTurnTheLastInGame(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isCurrentTurnTheLastInGame();
  }

  // line 95 "../../../../../Gameplay.ump"
   public boolean isSelectionValid(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isSelectionValid();
  }

  // line 99 "../../../../../Gameplay.ump"
   public boolean isDominoCorrectlyPreplaced(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isDominoCorrectlyPreplaced();
  }

  // line 103 "../../../../../Gameplay.ump"
   public boolean isImpossibleToPlace(){
    return !ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isPossibleToPlace();
  }


  /**
   * You may need to add more guards here
   * Actions
   */
  // line 113 "../../../../../Gameplay.ump"
   public void shuffleDominos(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.shuffleDominos();
  }

  // line 117 "../../../../../Gameplay.ump"
   public void setNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.setNextDraft();
  }

  // line 121 "../../../../../Gameplay.ump"
   public void orderNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.orderNextDraft();
        draftReady();
  }

  // line 126 "../../../../../Gameplay.ump"
   public void revealNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.revealNextDraft();
  }

  // line 130 "../../../../../Gameplay.ump"
   public void generateInitialPlayerOrder(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.generateInitialPlayerOrder();
  }

  // line 134 "../../../../../Gameplay.ump"
   public void nextPlayer(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.nextPlayer();
  }

  // line 138 "../../../../../Gameplay.ump"
   public void generatePlayerOrder(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.generatePlayerOrder();
  }

  // line 142 "../../../../../Gameplay.ump"
   public void placeDomino(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.placeDomino();
  }

  // line 146 "../../../../../Gameplay.ump"
   public void discardDomino(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.discardDomino();
  }

  // line 150 "../../../../../Gameplay.ump"
   public void calculatingScores(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.calculateRanking();
  }

}