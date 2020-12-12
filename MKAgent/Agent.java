package MKAgent;

public class Agent
{
  public final int DEPTH = 8;
  // public final int DECISION_TIME;

  public Board board;
  public Kalah kalah;
  public Side mySide;

  public Tree gameTree;

  public void playGame()
  {
    boolean canSwap = false;

    board = new Board(7,7);
    kalah = new Kalah(board);

    // South is the max player
    Tree gameTree = new Tree(board, Side.SOUTH);
    gameTree.generateChildrenLayers(DEPTH);

    while(true)
    {
      try
      {
        String message = Main.recvMsg();
        MsgType msgType = Protocol.getMessageType(message);

        switch(msgType)
        {
          case START:
            boolean isStarting = Protocol.interpretStartMsg(message);
            mySide = isStarting ? Side.SOUTH : Side.NORTH;

            if(isStarting)
            {
              Move optimalMove = runMinMax();
              kalah.makeMove(board, optimalMove);
              Main.sendMsg(Protocol.createMoveMsg(optimalMove.getHole()));
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

              gameTree = new Tree(board, mySide);
              gameTree.generateChildrenLayers(DEPTH);
            } // if

            if(moveTurn.again && !moveTurn.end)
            {
              if(canSwap && shouldSwap())
              {
                mySide = mySide.opposite();
                Main.sendMsg(Protocol.createSwapMsg());
                canSwap = false;

                gameTree = new Tree(board, mySide);
                gameTree.generateChildrenLayers(DEPTH);
              }
              else // can swap but doesnt swap or cant swap at all.
              {
                if(canSwap) canSwap = false;

                gameTree = gameTree.getChild(moveTurn.move);

                // Make your move
                Move nextMove = runMinMax();

                kalah.makeMove(board, nextMove);
                Main.sendMsg(Protocol.createMoveMsg(nextMove.getHole()));

                gameTree = gameTree.getChild(nextMove.getHole());
                gameTree.generateChildrenLayers(1);
              } // else
            } // if
            break;

          case END:
            return;

          default:
            System.err.println("Unknown state :(");
            break;
        } // switch
      } // try
      catch(Exception exception)
      {
        System.err.println(exception.getMessage());
      } // catch
    } // while

  } // playGame

  public Move runMinMax() throws Exception
  {
    return new Move(mySide, new Minimax(mySide).computeBestNextMove(gameTree, DEPTH-1));
  }

  public boolean shouldSwap()
  {
    Minimax minimax = new Minimax(mySide);
    int noSwapScore = minimax.advantageToSwap(mySide, board);
    int swapScore = minimax.advantageToSwap(mySide.opposite(), board);
    return swapScore > noSwapScore;
  } // shouldSwap
} // class Agent
