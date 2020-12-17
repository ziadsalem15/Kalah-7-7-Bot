package MKAgent;
import java.util.HashMap;

import java.io.FileWriter;
import java.io.IOException;

public class Agent
{
  public final int DEPTH = 7;
  // public final int DECISION_TIME;

  public Board board;
  // public Kalah kalah;
  public Side mySide;

  public Tree gameTree;

  public void playGame()
  {
    boolean canSwap = false;

    board = new Board(7,7);
    // kalah = new Kalah(board);

    // South is the max player
    gameTree = new Tree(board, Side.SOUTH);
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
              Kalah.makeMove(board, optimalMove);
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

              gameTree = new Tree(board, mySide);
              gameTree.generateChildrenLayers(DEPTH);
            } // if

            if(moveTurn.again)
            {
              if(canSwap && shouldSwap())
              {
                mySide = mySide.opposite();
                Main.sendMsg(Protocol.createSwapMsg());
                canSwap = false;

                Kalah.makeMove(board, new Move(mySide, moveTurn.move));

                gameTree = new Tree(board, mySide);
                gameTree.generateChildrenLayers(DEPTH);
              }
              else // can swap but doesnt swap or cant swap at all.
              {
                if(canSwap) canSwap = false;

                gameTree = new Tree(board, mySide);
                gameTree.generateChildrenLayers(DEPTH);

                // Make your move
                Move nextMove = runMinMax();

                Kalah.makeMove(board, nextMove);
                Main.sendMsg(Protocol.createMoveMsg(nextMove.getHole()));
              } // else
            } // if
            else
              Kalah.makeMove(board, new Move(mySide.opposite(), moveTurn.move));
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
    boolean maximizingPlayer = (mySide == Side.SOUTH);

    double bestHeuristicValue;
    if(maximizingPlayer)
      bestHeuristicValue = -Double.MAX_VALUE;
    else
      bestHeuristicValue = Double.MAX_VALUE;

    int bestMove = -1;
    Minimax minimax = new Minimax(mySide);

    for(int i = 1; i < board.getNoOfHoles()+1; i++)
    {
      Tree child = gameTree.getChild(i);

      if(child == null)
        continue;

      double minimaxVal = minimax.minimax(child, Integer.MIN_VALUE, Integer.MAX_VALUE, DEPTH);

      if(maximizingPlayer && minimaxVal > bestHeuristicValue)
      {
        bestHeuristicValue = minimaxVal;
        bestMove = i;
      } // if
      else if(!maximizingPlayer && minimaxVal < bestHeuristicValue)
      {
        bestHeuristicValue = minimaxVal;
        bestMove = i;
      } // else if
    } // for

    return new Move(mySide, bestMove);
  } // runMinMax

  public boolean shouldSwap()
  {
    Minimax minimax = new Minimax(mySide);
    double noSwapScore = minimax.advantageToSwap(mySide, board, DEPTH);
    double swapScore = minimax.advantageToSwap(mySide.opposite(), board, DEPTH);
    return swapScore > noSwapScore;
  } // shouldSwap
} // class Agent
