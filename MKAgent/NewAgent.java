public class NewAgent
{
  public final int DEPTH = 8;
  public final int DECISION_TIME;

  public Board board;
  public Kalah kalah;
  public Side mySide;

  public NewTree gameTree;

  public void playGame()
  {
    boolean canSwap = false;
    board = new Board(7,7);
    kalah = new Kalah(board);

    NewTree tree = new NewTree(new Board(board), Side.SOUTH);
    tree.generateChildrenLayers();

    while(true)
    {
      try
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
              canSwap = false;
              kalah.makeMove(board, new Move(mySide, 7));
              Main.sendMsg(Protocol.createMoveMsg(7));
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
              tree = new NewTree(new Board(board), mySide);
              tree.generateChildrenLayers();

            } // if
            if(moveTurn.again && !moveTurn.end)
            {
              if(canSwap && shouldSwap())
              {
                mySide = mySide.opposite();
                tree = new NewTree(new Board(board), mySide.opposite());
                tree.generateChildrenLayers();
                Main.sendMsg(Protocol.createSwapMsg());
                canSwap = false;
              }
              else // can swap but doesnt swap or cant swap at all.
              {
                if(canSwap) canSwap = false;
                tree = tree.getChild(moveTurn.move);
                tree.generateChildrenLayers();
                Move nextMove = runMinMax(tree);
                kalah.makeMove(board, nextMove);
                Main.sendMsg(Protocol.createMoveMsg(nextMove.getHole()));

              } // else
            }// if
            else
            {
              tree = tree.getChild(moveTurn.move);
              tree.generateChildrenLayers();
            }
            break;
          case END:
            return;
          default:
            System.err.println("Unknown state :(");
            break;
        } // switch
      }
      catch(Exception exception)
      {
        System.err.println(exception.getMessage());
      } // catch
    } // while

  } // playGame

  public Move runMinMax(NewTree tree) throws Exception
  {
    return new Move(mySide, NewMinimax.minimax(tree, Integer.MIN_VALUE, Integer.MAX_VALUE, DEPTH));
  }

  public boolean shouldSwap()
  {
    NewMinimax minimax = new NewMinimax(mySide);
    int noSwapScore = minimax.advantageToSwap(mySide, board);
    int swapScore = minimax.advantageToSwap(mySide.opposite(), board);
    return swapScore > noSwapScore;
  } // shouldSwap
} // class Agent
