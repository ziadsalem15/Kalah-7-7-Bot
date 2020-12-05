public class Minimax
{

  // TODO: Implement minimax with alpha-beta pruning
  //       Add heuristics and optimise the search.

  public Side ourSide;
  public Tree tempNode;
  public Tree bestValue;
  public long startTime;
   public long limitTime;
  public Minimax(Side side, long stTime, long timeLimit)
  {
    this.ourSide = side;
    this.startTime = stTime;
    this.limitTime = timeLimit;
  }
  public Tree minimax(Tree node, int alpha, int beta)
  {
    if(node.children.isEmpty())
    {
      return node.value;
    }
    else
    {
      // if our side then its a maximizing node
      if(node.side == ourSide)
      {
        // we update alpha
        bestValue.value = Integer.MIN_VALUE;
        // checking it it has childeren
        while(node.getChildren().hasNext())
        {
          tempNode = minimax(node.getChildren().next(), alpha,beta);
          bestValue.value = Math.max(bestValue.value, tempNode);
          alpha = Math.max(alpha, bestValue.value);
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
          while(node.getChildren().hasNext())
          {
            tempNode = minimax(node.getChildren().next(), alpha,beta);
            bestValue.value = Math.min(bestValue.value, tempNode);
            beta = Math.min(beta, bestValue.value);
            if (beta <= alpha)
              break;
          }
        }
        return bestValue;
    }
  } // minimax

  public BestMove runMove()
  {
    
  }
}  // class Minimax
