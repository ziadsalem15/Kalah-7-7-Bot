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

        case STATE:
          Protocol.MoveTurn moveTurn = Protocol.interpretStateMsg(message, board);

          if(moveTurn.end)
            return;

          if(moveTurn.again && moveTurn.move == -1)
            mySide = mySide.opposite();

          if(moveTurn.again && !moveTurn.end)
          {
            if(canSwap && shouldSwap())
            {
              mySide = mySide.opposite();
              Main.sendMsg(Protocol.createSwapMsg());
              canSwap = false;
            }
            else // can swap but doesnt swap or cant swap at all.
            {
              if(canSwap) canSwap = false;

              // Make your move
              Move nextMove = runMinMax();
              Main.sendMsg(Protocol.createMoveMsg(nextMove.getHole()));
            } // else
          } // if
          break;

        default:
          System.err.println("Unknown state :(");
          break;
      } // switch
    } // while

  } // playGame

  public Move runMinMax() throws Exception
  {
    return new Move(mySide, Minimax.computeBestNextMove(this.kalah.getBoard()));
  }

  public boolean shouldSwap()
  {
    Minimax minimax = new Minimax(this.side);
    int noSwapScore = minimax.advantageToSwap(this.side, this.board);
    int swapScore = minimax.advantageToSwap(this.side.opposite(), this.board);
    return swapScore > noSwapScore;
  } // shouldSwap
} // class Agent
