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
            sendMsg(Protocol.createMoveMsg(2));
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
            Move nextMove = runMinMax(tree);
            Main.sendMsg(Protocol.createMoveMsg(nextMove.getHole()));
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
