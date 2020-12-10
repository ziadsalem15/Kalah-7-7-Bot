package MKAgent;

public class Agent
{
  public final int DEPTH = 8;
  public final int DECISION_TIME;

  public Board board;
  public Kalah kalah;
  public Side mySide;

  public Tree gameTree;

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
        case START:
          boolean isStarting = Protocol.interpretStartMessage(message);
          mySide = isStarting ? Side.SOUTH : Side.NORTH;

          if(isStarting)
          {
            Tree temp = new Tree(board, null, mySide);
            gameTree = Tree.generateChildrenLayers(temp, DEPTH);

            Move optimalMove = runMinMax();
            kalah.makeMove(board, optimalMove);
            sendMsg(Protocol.createMoveMsg(optimalMove.getHole()));
            canSwap = false;
          } // if
          else
            canSwap = true;
          break;

        case STATE:
          Protocol.MoveTurn moveTurn = Protocol.interpretStateMsg(message, board);

          if(moveTurn.end)
            return;

          if(moveTurn.again && moveTurn.move == -1) // player 1
          {
            mySide = mySide.opposite();
          } // if

          if(moveTurn.again && !moveTurn.end)
          {
            if(canSwap && shouldSwap())
            {
              mySide = mySide.opposite();
              Main.sendMsg(Protocol.createSwapMsg());
              canSwap = false;

              Tree temp = new Tree(board, null, mySide);
              gameTree = Tree.generateChildrenLayers(temp, DEPTH);
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

        case END:
          return;

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
