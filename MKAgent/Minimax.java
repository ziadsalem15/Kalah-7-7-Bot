package MKAgent;
import java.util.ArrayList;
import java.util.List;

import java.io.FileWriter;
import java.io.IOException;

public class Minimax
{
  // TODO: Implement minimax with alpha-beta pruning
  //       Add heuristics and optimise the search.

  public Side ourSide;
  public int tempValue;
  public int bestValue;
  private static int MINIMAX_DEPTH = 10;


  public Minimax(Side side)
  {
    this.ourSide = side;
  }

  public int advantageToSwap(Side side, Board board)
  {
    List<Integer> indicesOfNonEmptyHoles = board.getIndicesOfNonEmptyHoles(side);
    int maxScore = Integer.MIN_VALUE;

     for(Integer index : indicesOfNonEmptyHoles)
     {
        Move chMove = new Move(side, index);
        Board chBoard = new Board(board);
        Side chSide = Kalah.makeMove(chBoard, chMove);
        Tree chTree = new Tree(chBoard, chSide);
        int chScore = minimax(chTree, Integer.MIN_VALUE, Integer.MAX_VALUE, MINIMAX_DEPTH);
        maxScore = Math.max(chScore, maxScore);
     }
     return maxScore;
  }

  // public int computeBestNextMove(Tree tree, int depth)
  // {
  //
  //   ArrayList<Tree> children = tree.getChildren();
  //
  //   // Initialise to random values.
  //   int bestMove = -1;
  //   int bestHeuristicValue = -1;
  //   Side childSide = tree.getSide().opposite();
  //
  //   for(int i = 0; i < children.size(); i++)
  //   {
  //     Tree child = children.get(i);
  //     int minimaxVal = minimax(child, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
  //
  //     if(minimaxVal > bestHeuristicValue)
  //     {
  //       bestHeuristicValue = minimaxVal;
  //       bestMove = i+1;
  //     } // if
  //   } // for
  //
  //   return bestMove;
  // } // computeBestNextMove

  public int minimax(Tree node, int alpha, int beta, int depth)
  {
    if(node.getChildren().isEmpty() || depth == 0)
    {
      int tmep = (int)Math.round(new Heuristics().getHeuristicScore(node));

      try {
      FileWriter myWriter = new FileWriter("./filename.txt");
      myWriter.write("heuristic value: " + tmep);
      myWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

      return tmep;
    }
    else
    {
      // if our side then its a maximizing node
      if(node.getSide() == ourSide)
      {
        // we update alpha
        bestValue = Integer.MIN_VALUE;

        // checking it it has childeren
        // for(Tree child : node.getChildren())
        for(int i=1; i<8; i++)
        {
          Tree child = node.getChild(i);
          tempValue = minimax(child, alpha, beta, depth-1);
          bestValue = Math.max(bestValue, tempValue);
          alpha = Math.max(alpha, bestValue);
          if (beta <= alpha)
            break;
        }
      }
      else // openent player
      {
        // udpating beta
        bestValue = Integer.MAX_VALUE;

        // checking it it has childeren
        for(int i=1; i<8; i++)
        {
          Tree child = node.getChild(i);
          tempValue = minimax(child, alpha, beta, depth-1);
          bestValue = Math.min(bestValue, tempValue);
          beta = Math.min(beta, bestValue);
          if (beta <= alpha)
            break;
        }
      }
      return bestValue;
    } // else
  } // minimax

}  // class Minimax
