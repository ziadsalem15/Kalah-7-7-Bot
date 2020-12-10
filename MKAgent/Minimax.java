package MKAgent;

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

    Iterator iteratorOnNonEmpty = indicesOfNonEmptyHoles.iterator();

     while(iteratorOnNonEmpty.hasNext())
     {
        int index = (Integer)iteratorOnNonEmpty.next();
        Move chMove = new Move(side, index);
        Board chBoard = new Board(board);
        Side chSide = Kalah.makeMove(childBoard, childMove);
        Tree chTree = new Tree(chBoard, chSide);
        int chScore = minimax(chTree, Integer.MIN_VALUE, Integer.MAX_VALUE, MINIMAX_DEPTH);
        if (chScore > maxScore)
        {
           maxScore = chScore;
        }
     }
     return maxScore;
  }
  public int computeBestNextMove(Board board)
  {
    
  }
  public int minimax(Tree node, int alpha, int beta, int depth)
  {
    if(node.getChildren().isEmpty())
    {
      return Heuristics.getHeuristicScore(node);
    }
    else
    {
      // if our side then its a maximizing node
      if(node.getSide() == ourSide)
      {
        // we update alpha
        bestValue = Integer.MIN_VALUE;

        // checking it it has childeren
        for(Tree child : node.getChildren())
        {
          tempValue = minimax(child, alpha, beta, depth-1);
          bestValue = Math.max(bestValue, tempValue);
          alpha = Math.max(alpha, bestValue);
          if (beta <= alpha)
            break;
          }
        }
        // openent player
        else
        {
          // udpating beta
          bestValue = Integer.MAX_VALUE;

          // checking it it has childeren
          for(Tree child : node.getChildren())
          {
            tempValue = minimax(child, alpha, beta, depth-1);
            bestValue = Math.min(bestValue, tempValue);
            beta = Math.min(beta, bestValue);
            if (beta <= alpha)
              break;
          }
        }
        return bestValue;
    }
  } // minimax

}  // class Minimax
