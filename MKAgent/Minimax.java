package MKAgent;

public class Minimax
{
  // TODO: Implement minimax with alpha-beta pruning
  //       Add heuristics and optimise the search.

  public Side ourSide;
  public int tempValue;
  public int bestValue;
  // public long startTime;
  // public long limitTime;

  public Minimax(Side side)
  {
    this.ourSide = side;
    // this.startTime = stTime;
    // this.limitTime = timeLimit;
  }

  // public int evaluate(Heuristics heuristicValue)
  // {
  //   return heuristicValue.heuristic(this.ourSide);
  // }

  public int minimax(Tree node, int alpha, int beta)
  {
    if(node.getChildren().isEmpty())
    {
      return node.getHeuristicScore();
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
          tempValue = minimax(child, alpha, beta);
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
            tempValue = minimax(child, alpha, beta);
            bestValue = Math.min(bestValue, tempValue);
            beta = Math.min(beta, bestValue);
            if (beta <= alpha)
              break;
          }
        }
        return bestValue;
    }
  } // minimax

  // public BestMove runMove()
  // {
  //
  // }
}  // class Minimax
