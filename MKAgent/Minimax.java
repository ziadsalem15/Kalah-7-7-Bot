package MKAgent;
import java.util.HashMap;
import java.util.List;

import java.io.FileWriter;
import java.io.IOException;

public class Minimax
{
  // TODO: Implement minimax with alpha-beta pruning
  //       Add heuristics and optimise the search.

  public Side ourSide;

  public Minimax(Side side)
  {
    this.ourSide = side;
  }

  public double advantageToSwap(Side side, Board board, int depth)
  {
    List<Integer> indicesOfNonEmptyHoles = board.getIndicesOfNonEmptyHoles(side);
    double maxScore = Integer.MIN_VALUE;

     for(Integer index : indicesOfNonEmptyHoles)
     {
        Move chMove = new Move(side, index);
        Board chBoard = new Board(board);
        Side chSide = Kalah.makeMove(chBoard, chMove);
        Tree chTree = new Tree(chBoard, chSide);
        double chScore = minimax(chTree, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
        maxScore = Math.max(chScore, maxScore);
     }
     return maxScore;
  }

  public double minimax(Tree node, double alpha, double beta, int depth)
  {
    double bestValue = 0;

    if(node.getChildren().isEmpty() || depth == 0)
      return new Heuristics().getHeuristicScore(node);

    else
    {
      // if our side then its a maximizing node
      if(node.getSide() == ourSide)
      {
        // we update alpha
        bestValue = Integer.MIN_VALUE;

        // checking it it has childeren
        for(Tree child : node.getChildren().values())
        {
          double tempValue = minimax(child, alpha, beta, depth-1);
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
        for(Tree child : node.getChildren().values())
        {
          double tempValue = minimax(child, alpha, beta, depth-1);
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
