package MKAgent;
import java.util.ArrayList;

import java.io.FileWriter;
import java.io.IOException;

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
    gameTree = new Tree(board, Side.SOUTH, 0);
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

              gameTree = new Tree(board, mySide, 0);
              gameTree.generateChildrenLayers(DEPTH);
            } // if

            if(moveTurn.again && !moveTurn.end)
            {
              if(canSwap && shouldSwap())
              {
                mySide = mySide.opposite();
                Main.sendMsg(Protocol.createSwapMsg());
                canSwap = false;

                gameTree = new Tree(board, mySide, 0);
                gameTree.generateChildrenLayers(DEPTH);
              }
              else // can swap but doesnt swap or cant swap at all.
              {
                if(canSwap) canSwap = false;

                // gameTree = gameTree.getChild(moveTurn.move);
                // gameTree.generateBottomLayer1(8);

                gameTree = new Tree(board, mySide, 0);
                gameTree.generateChildrenLayers(DEPTH);
                // Make your move
                Move nextMove = runMinMax();

                kalah.makeMove(board, nextMove);
                Main.sendMsg(Protocol.createMoveMsg(nextMove.getHole()));

                //gameTree = gameTree.getChild(nextMove.getHole());
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
    int bestMove = -1;
    int bestHeuristicValue = -1;
    Minimax minimax = new Minimax(mySide);

    for(int i = 1; i < board.getNoOfHoles()+1; i++)
    {
      Tree child = gameTree.getChild(i);
      int minimaxVal = minimax.minimax(child, Integer.MIN_VALUE, Integer.MAX_VALUE, DEPTH-1);
        try {
        FileWriter myWriter = new FileWriter("./filename22.txt");
        myWriter.write("heuristic value: " + minimaxVal + " " + i);
        myWriter.close();
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
      if(minimaxVal > bestHeuristicValue)
      {

        bestHeuristicValue = minimaxVal;
        bestMove = i;
      } // if
    } // for

    return new Move(mySide, bestMove);
  } // runMinMax

  public boolean shouldSwap()
  {
    Minimax minimax = new Minimax(mySide);
    int noSwapScore = minimax.advantageToSwap(mySide, board);
    int swapScore = minimax.advantageToSwap(mySide.opposite(), board);
    return swapScore > noSwapScore;
  } // shouldSwap
} // class Agent
