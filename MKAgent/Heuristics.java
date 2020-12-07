package MKAgent;

public class Heuristics
{
  
  public int getDifferenceBetweenWells(Side ourSide)
  {
    int difference = getSeedsInStore(ourSide) - getSeedsInStore(ourSide.opposite());
  }

  public int heuristic(Side side)
  {
    int weightH1 = 0.45;
    int value = getDifferenceBetweenWells(side) * weightH1;
    return value
  }

} // class Heuristics
