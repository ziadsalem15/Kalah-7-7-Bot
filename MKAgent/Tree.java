package MKAgent;

import java.util.ArrayList;
import java.util.Iterator;

public class Tree
{
  private int heuristicScore;
  private final ArrayList<Tree> children;
  private Tree parent;

  private final Board board;
  private int depth;
  private Side side;

  // Constructor
  public Tree(Board board, Tree parent, Side side)
  {
    // TODO: Set the heuristic score
    children = new ArrayList<Tree>();

    // You shouldn't be able to change those values.
    this.parent = parent;
    this.board = board;
    this.side = side;

    setDepth(parent);
  }

  // Set the height of the tree by looking at its
  // deepest child. A way around always keeping this
  // up to date is not having an instance variable,
  // and so just returning the height on demand.
  private static int getHeight(Tree tree)
  {
    if(tree.children.size() == 0)
      return tree.depth + 1;

    int max = 0;
    for(Tree child : tree.children)
      max = Math.max(max, getHeight(child));

    return max;
  }

  public void setHeuristicScore(int newScore)
  {
    heuristicScore = newScore;
  } // setHeuristicScore

  public void addChild(Tree child)
  {
    children.add(child);
  } // addChild

  private void setDepth(Tree parentNode)
  {
    if(parentNode == null)
      this.depth = 0;
    else
      this.depth = parentNode.getDepth() + 1;
  } // setDepth

  public boolean removeChild(Tree child)
  {
    return children.remove(child);
  } // removeChild

  // Get the root of the tree.
  public int getHeuristicScore()
  {
    return heuristicScore;
  } // getRoot

  public ArrayList<Tree> getChildren()
  {
    return new ArrayList<Tree>(children);
  } // getChildren

  public Tree getParent()
  {
    return parent;
  } // getParent

  public Board getBoard()
  {
    return board;
  } // getBoard

  public int getDepth()
  {
    return depth;
  } // getDepth

  public Side getSide()
  {
    return side;
  } // getSide

} // class Tree
