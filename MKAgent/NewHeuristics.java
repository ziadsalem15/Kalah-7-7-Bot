package MKAgent;

public class NewHeuristics
{
  private int getDifferenceBetweenWells(NewTree tree)
  {
    Side side = tree.getSide();
    Board board = tree.getBoard();

    int difference = 0;
    if(side == Side.SOUTH)
      difference = board.getSeedsInStore(side) - board.getSeedsInStore(side.opposite());
    else
      difference = board.getSeedsInStore(side.opposite()) - board.getSeedsInStore(side);

    return difference;
  }

  public int extraTurn(NewTree tree)
  {
    Side side = tree.getSide();
    Board board = tree.getBoard();
    int noOfHoles =  board.getNoOfHoles();
    int numOfExtraTurn = 0;
    for(int hole = 1; hole < noOfHoles+1; hole++)
    {
       if ((board.getSeeds(side, hole) % 15) == ((noOfHoles+1) - hole))
       {
          numOfExtraTurn++;
       }
    }
    return numOfExtraTurn;
   }

  private int getCapturingOportunities(NewTree tree, boolean opposite)
  {
    Side side;
    if(opposite)
      side = tree.getSide().opposite();
    else
      side = tree.getSide();

    Board board = tree.getBoard();
    int noOfHoles = board.getNoOfHoles();

    // int maxCapture = 0
    int captureCount = 0;
    for(int i = 0; i < noOfHoles; i++)
    {
      int noOfSeeds = board.getSeeds(side, i+1);
      int endHole = -1;

      if(noOfSeeds != 0)
      {
        noOfSeeds = noOfSeeds - (noOfHoles - (i+1));
        if(noOfSeeds <= 0) // we are still on our side
          endHole = noOfHoles + noOfSeeds;
        else
          noOfSeeds = noOfSeeds - (noOfHoles + 1);
          if(noOfSeeds > 0) // we are back on our side
            endHole = noOfSeeds;
      } // if

      if(endHole != -1) // you ended up in your side
      {
        int remainingSeeds = board.getSeeds(side, endHole) + 1;
        if(remainingSeeds == 1) // that's a capturing oportunity
          // maxCapture = Math.max(board.getSeedsOp(side, endHole), maxCapture);
          captureCount++;
      } // if
    } // for

    return captureCount;
  } // getCapturingOportunities

  public double getHeuristicScore(NewTree tree)
  {
    double weights[] = {0.45, 0.05, -0.1, 0.1};
    double heuristicScore = getDifferenceBetweenWells(tree) * weights[0]
                           + getCapturingOportunities(tree, false) * weights[1]
                           + getCapturingOportunities(tree, true) * weights[2]
                           + extraTurn(tree) * weights[3];
    return heuristicScore;
  } // getHeuristicScore

} // class Heuristics
