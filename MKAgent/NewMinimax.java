package MKAgent;
import java.util.ArrayList;
import java.util.List;

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

  // public int computeBestNextMove(NewTree tree, int depth)
  // {
  //   Map<Integer, NewTree> children = tree.getChildren();
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
      return (int)Math.round(new Heuristics().getHeuristicScore(node));
    }
    else
    {
      // if our side then its a maximizing node
      if(node.getSide() == ourSide)
      {
        // we update alpha
        bestValue = Integer.MIN_VALUE;

        // checking it it has childeren
        for(NewTree child : node.getChildren().keySet())
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
        for(NewTree child : node.getChildren().keySet())
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
