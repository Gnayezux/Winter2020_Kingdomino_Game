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
        revealNextDraft(); generateInitialPlayerOrder();
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
        // line 34 "../../../../../Gameplay.ump"
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
        // line 29 "../../../../../Gameplay.ump"
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
          setNextDraft(); orderNextDraft(); generatePlayerOrder();
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
        // line 30 "../../../../../Gameplay.ump"
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
        // line 22 "../../../../../Gameplay.ump"
          placeDomino(); updateScore();
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
        // line 23 "../../../../../Gameplay.ump"
          discardDomino(); updateScore();
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
        // line 24 "../../../../../Gameplay.ump"
          placeDomino(); updateScore(); nextPlayer();
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
        // line 25 "../../../../../Gameplay.ump"
          discardDomino(); updateScore(); nextPlayer();
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
      case PlacingDomino:
        // line 21 "../../../../../Gameplay.ump"
        revealNextDraft();
        break;
      case CreatingDraft:
        // line 33 "../../../../../Gameplay.ump"
        setNextDraft(); orderNextDraft(); revealNextDraft();
        break;
      case EndingGame:
        // line 37 "../../../../../Gameplay.ump"
        calculatingScores();
        break;
    }
  }

  public void delete()
  {}


  /**
   * Setter for test setup
   */
  // line 47 "../../../../../Gameplay.ump"
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
       	case "EndingGame":
       		gamestatus = Gamestatus.Playing;
       	    gamestatusInitializing = GamestatusInitializing.Null;
       	    gamestatusPlaying = GamestatusPlaying.EndingGame;
       	    setGamestatusPlaying(GamestatusPlaying.EndingGame);
       	    break;     	    
       	default:
       	    throw new RuntimeException("Invalid gamestatus string was provided: " + status);
       	}
  }


  /**
   * Guards
   */
  // line 94 "../../../../../Gameplay.ump"
   public boolean isCurrentPlayerTheLastInTurn(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isCurrentPlayerTheLastInTurn();
  }

  // line 98 "../../../../../Gameplay.ump"
   public boolean isCurrentTurnTheLastInGame(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isCurrentTurnTheLastInGame();
  }

  // line 102 "../../../../../Gameplay.ump"
   public boolean isSelectionValid(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isSelectionValid();
  }

  // line 106 "../../../../../Gameplay.ump"
   public boolean isDominoCorrectlyPreplaced(){
    return ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isDominoCorrectlyPreplaced();
  }

  // line 110 "../../../../../Gameplay.ump"
   public boolean isImpossibleToPlace(){
    return !ca.mcgill.ecse223.kingdomino.controller.KingdominoController.isPossibleToPlace();
  }


  /**
   * You may need to add more guards here
   * Actions
   */
  // line 120 "../../../../../Gameplay.ump"
   public void shuffleDominos(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.shuffleDominos();
  }

  // line 124 "../../../../../Gameplay.ump"
   public void setNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.setNextDraft();
  }

  // line 128 "../../../../../Gameplay.ump"
   public void orderNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.orderNextDraft();
        draftReady();
  }

  // line 133 "../../../../../Gameplay.ump"
   public void revealNextDraft(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.revealNextDraft();
  }

  // line 137 "../../../../../Gameplay.ump"
   public void generateInitialPlayerOrder(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.generateInitialPlayerOrder();
  }

  // line 141 "../../../../../Gameplay.ump"
   public void nextPlayer(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.nextPlayer();
  }

  // line 145 "../../../../../Gameplay.ump"
   public void generatePlayerOrder(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.generatePlayerOrder();
  }

  // line 149 "../../../../../Gameplay.ump"
   public void placeDomino(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.placeDomino();
  }

  // line 153 "../../../../../Gameplay.ump"
   public void discardDomino(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.discardDomino();
  }

  // line 157 "../../../../../Gameplay.ump"
   public void calculatingScores(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.calculateRanking();
  }

  // line 161 "../../../../../Gameplay.ump"
   public void updateScore(){
    ca.mcgill.ecse223.kingdomino.controller.KingdominoController.updateScore();
  }

}