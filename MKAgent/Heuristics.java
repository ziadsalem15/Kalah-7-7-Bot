package MKAgent;

public class Heuristics
{
  private int getDifferenceBetweenWells(Tree tree)
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

  public int extraTurn(Tree tree)
  {
    Side side = tree.getSide();
    Board board = tree.getBoard();
    int noOfHoles =  board.getNoOfHoles();
    int numOfExtraTurn = 0;

    for(int hole = 1; hole < noOfHoles+1; hole++)
       if ((board.getSeeds(side, hole) % 15) == ((noOfHoles+1) - hole))
          numOfExtraTurn++;

    return numOfExtraTurn;
   }

  private int getCapturingOportunities(Tree tree, boolean opposite)
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
    for(int i = 1; i < noOfHoles+1; i++)
    {
      int noOfSeeds = board.getSeeds(side, i);
      int endHole = -1;

      if(noOfSeeds != 0)
      {
        // noOfSeeds = noOfSeeds - (noOfHoles - (i+1));
        // if(noOfSeeds <= 0) // we are still on our side
        //   endHole = noOfHoles + noOfSeeds;
        // else
        //   noOfSeeds = noOfSeeds - (noOfHoles + 1);
        //   if(noOfSeeds > 0) // we are back on our side
        //     endHole = noOfSeeds;

        int offset = noOfSeeds%15;
        if(offset <= noOfHoles - i)
          endHole = i + offset;
        else if(offset > noOfHoles*2+1-i)
          endHole = i - 15 + offset;
      } // if

      if(endHole > 0) // you ended up in your side
      {
        int remainingSeeds = board.getSeeds(side, endHole) + 1;
        if(remainingSeeds == 1) // that's a capturing oportunity
          // maxCapture = Math.max(board.getSeedsOp(side, endHole), maxCapture);
          captureCount++;
      } // if
    } // for

    return captureCount;
  } // getCapturingOportunities

  public double getHeuristicScore(Tree tree)
  {
    double weights[] = {0.485, 0.269, -0.565, 0.421};
    double heuristicScore = getDifferenceBetweenWells(tree) * weights[0]
                           + getCapturingOportunities(tree, false) * weights[1]
                           + getCapturingOportunities(tree, true) * weights[2]
                           + extraTurn(tree) * weights[3];
    return heuristicScore;
  } // getHeuristicScore

  // a good strategy is to start with the rightmost seedsCloseToOurScroing
  // so divide the number of holes by three to get leftmost, middle, rightmost
  public int seedsCloseToOurScoring(Tree tree) {
    Board board = tree.getBoard();
    Side side = tree.getSide();

    int holesNum = board.getNoOfHoles();
    int seedsNum = 0;

    for(int hole = holesNum - holesNum/3; hole <= holesNum; hole++) {
       seedsNum += board.getSeeds(side, hole);
    }

    return seedsNum;
   }

   // this strategy will allow you to keep scoring in your scoring well
   public int numberOfNonEmptyHoles(Board board, Side side) {
      int count = 0;
      int holesNum = board.getNoOfHoles();

      for(int hole = 1; hole <= holesNum; hole++) {
         if (board.getSeeds(side, hole) > 0) {
           count++;
         }
      }

      return count;
   }

   // public int moreSeeds(Tree tree)
   // {
   //
   // } // moreSeeds

} // class Heuristics
