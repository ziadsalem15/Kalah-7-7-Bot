package MKAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NewMinimax
{
  // TODO: Implement minimax with alpha-beta pruning
  //       Add heuristics and optimise the search.

  public Side ourSide;
  public int tempValue;
  public int bestValue;
  private static int MINIMAX_DEPTH = 10;


  public NewMinimax(Side side)
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
        NewTree chTree = new NewTree(chBoard, chSide);
        int chScore = minimax(chTree, Integer.MIN_VALUE, Integer.MAX_VALUE, MINIMAX_DEPTH);
        maxScore = Math.max(chScore, maxScore);
     }
     return maxScore;
  }

  public int computeBestNextMove(NewTree tree, int depth)
  {
    Map<Integer, NewTree> children = tree.getChildren();

    // Initialise to random values.
    int bestMove = -1;
    int bestHeuristicValue = -1;
    //Side childSide = tree.getSide().opposite();

    for(int i = 0; i < tree.getBoard().getValidHoles(tree.getSide()).size(); i++)
    {
      NewTree child = children.get(i);
      int minimaxVal = minimax(tree, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);

      if(minimaxVal > bestHeuristicValue)
      {
        bestHeuristicValue = minimaxVal;
        bestMove = i+1;
      } // if
    } // for

    return bestMove;
  } // computeBestNextMove

  public int minimax(NewTree node, int alpha, int beta, int depth)
  {

    if(node.getChildren().isEmpty() || depth == 0)
    {
      int best = (int)Math.round(new NewHeuristics().getHeuristicScore(node));
      return best;
    }
    else
    {
      // if our side then its a maximizing node
      if(node.getSide() == ourSide)
      {
        // we update alpha
        bestValue = Integer.MIN_VALUE;

        // checking it it has childeren
        for(NewTree child : node.getChildren().values())
        {

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
        for(NewTree child : node.getChildren().values())
        {
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