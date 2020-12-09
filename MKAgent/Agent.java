package MKAgent;

public class Agent
{
  public final int DEPTH = 10;
  public final int DECISION_TIME;

  public Board board;
  public Kalah kalah;
  public Side mySide;

  public void playGame()
  {
    canSwap = false;

    board = new Board(7,7);
    kalah = new Kalah(board);

    while(true)
    {
      String message = Main.recvMsg();
      MsgType msgType = Protocol.getMessageType(message);

      switch(msgType)
      {
        case END:
          return;

        case STATE:
          Protocol.MoveTurn moveTurn = Protocol.interpretStateMsg(message, board);

          if(moveTurn.end)
            return;

          if(moveTurn.again && moveTurn.move == -1)
            mySide = mySide.opposite();

          if(moveTurn.again)
          {
            if(canSwap && shouldSwap())
            {
              mySide = mySide.opposite();
              Main.sendMsg(Protocol.createSwapMsg());
            }
            else // can swap but doesnt swap or cant swap at all.
            {
              if(canSwap) canSwap = false;

              // Make your move
              runMinMax();
              Main.sendMsg(Protocol.createMoveMsg());
            } // else
          } // if
          break;

        case START:
          boolean isStarting = Protocol.interpretStartMessage(message);
          mySide = isStarting ? Side.SOUTH : Side.NORTH;

          if(isStarting)
          {
            kalah.makeMove(board, new Move(mySide, 7));
            sendMsg(Protocol.createMoveMsg(7));
            canSwap = false;
          } // if
          else
            canSwap = true;
          break;

        default:
          System.err.println("Unknown state :(");
          break;
      } // switch
    } // while

  } // playGame

  public void runMinMax() throws Exception
  {
    // Board newboard = this.board.clone();
    // new BestMove(0, 0.0D);
    long startTime = System.currentTimeMillis();
    long timeLimit = 20000L;
    Minimax nextMove = new Minimax(this.side, startTime, timeLimit);
    BestMove optimalMove = nextMove.runMove();

    if(kalah.isLegalMove(optimalMove) && !kalah.gameOver())
      kalah.makeMove(this.board, new Move(this.side, optimalMove.hole));
    else
      throw new IllegalArgumentException("You are performing an illegal move!");

    Main.sendMsg(Protocol.createMoveMsg(optimalMove.hole));
  }

  public boolean shouldSwap()
  {
    Minimax minimax = new Minimax(this.side);
    int noSwapScore = minimax.advantageToSwap(this.side, this.board);
    int swapScore = minimax.advantageToSwap(this.side.opposite(), this.board);
    return swapScore > noSwapScore;
  } // shouldSwap
} // class Agent
