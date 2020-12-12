package MKAgent;
import java.util.ArrayList;

public class Tree
{
  private final ArrayList<Tree> children;

  private final Board board;
  private Side side;

  // Constructor
  public Tree(Board board, Side side)
  {
    // TODO: Set the heuristic score
    children = new ArrayList<Tree>();

    // You shouldn't be able to change those values.
    this.board = board;
    this.side = side;
  }

  public void addChild(Tree child)
  {
    children.add(child);
  } // addChild

  // public static void generateChildrenLayers(Tree tree, int noOfLayers)
  // {
  //   int noOfChildren = board.getNoOfHoles();
  //
  //   if(noOfLayers == 1)
  //     for(int i = 0; i < noOfChildren; i++)
  //       tree.addChild(new Tree(board, tree.getSide().opposite()));
  //
  //   else if(noOfLayers > 1)
  //     for(int i = 0; i < noOfChildren; i++)
  //     {
  //       Tree child = new Tree(board, tree.getSide().opposite());
  //       tree.addChild(child);
  //       generateChildrenLayers(child, noOfLayers-1);
  //     } // for
  // } // generateChildren

  public void generateChildrenLayers(int noOfLayers)
  {
    int noOfChildren = board.getNoOfHoles();

    if(noOfLayers == 1)
      for(int i = 0; i < noOfChildren; i++)
        this.addChild(new Tree(board, side.opposite()));

    else if(noOfLayers > 1)
      for(int i = 0; i < noOfChildren; i++)
      {
        Tree child = new Tree(board, side.opposite());
        this.addChild(child);
        child.generateChildrenLayers(noOfLayers-1);
      } // for
  } // generateChildren

  public boolean removeChild(Tree child)
  {
    return children.remove(child);
  } // removeChild

  public ArrayList<Tree> getChildren()
  {
    return new ArrayList<Tree>(children);
  } // getChildren

  public Tree getChild(int holeNumber)
  {
    return children.get(holeNumber-1);
  } // getChild

  public Board getBoard()
  {
    return board;
  } // getBoard

  public Side getSide()
  {
    return side;
  } // getSide

} // class Tree
