package MKAgent;
import java.util.ArrayList;

import java.io.FileWriter;
import java.io.IOException;


public class Tree
{
  private final ArrayList<Tree> children;

  private final Board board;
  private Side side;

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
      {
        Board state = new Board(this.board);
        Move nextMove = new Move(side, i+1);

        if(!Kalah.isLegalMove(state, nextMove))
          continue;

        Side tmpSide = Kalah.makeMove(state, nextMove);
        this.addChild(new Tree(state, tmpSide));
      } // for

    else if(noOfLayers > 1)
      for(int i = 0; i < noOfChildren; i++)
      {
        Board state = new Board(this.board);
        Move nextMove = new Move(side, i+1);

        if(!Kalah.isLegalMove(state, nextMove))
          continue;

        Side tmpSide = Kalah.makeMove(state, nextMove);

        Tree child = new Tree(state, tmpSide);
        this.addChild(child);
        child.generateChildrenLayers(noOfLayers-1);
      } // for
  } // generateChildren


  // public ArrayList<Tree> getChildrenAtDepth(int depth, Tree tree)
  // {
  //
  //   ArrayList<Tree> children = new ArrayList();
  //   if (tree.getChild(1).depth == depth)
  //   {
  //
  //     for (int i = 0; i < board.getNoOfHoles(); i++)
  //     {
  //       Tree child = tree.getChild(i+1);
  //       children.add(child);
  //     }
  //   }
  //   else
  //   {
  //
  //     for (int i = 0; i < board.getNoOfHoles(); i++)
  //     {
  //
  //       getChildrenAtDepth(depth, tree.getChild(i+1));
  //     }
  //   }
  //
  //   return children;
  // }


  // public void generateBottomLayer()
  // {
  //   if(this.children.size() == 0)
  //     for(int i = 0; i < board.getNoOfHoles(); i++)
  //     {
  //       Board temp = new Board(this.board);
  //       new Kalah(temp).makeMove(new Move(side, i+1));
  //
  //       this.addChild(new Tree(temp, side.opposite(), ));
  //     } // for
  //   else
  //   {
  //     for(Tree child : this.children)
  //       child.generateBottomLayer();
  //   }
  // } // generateBottomLayer


  // public void generateBottomLayer1(int depth)
  // {
  //   ArrayList<Tree> leaves = new ArrayList();
  //   leaves = getChildrenAtDepth(depth, this);
  //
  //   Tree leaf1 = leaves.get(1);
  //   if (leaf1.children.size() == 0)
  //   {
  //     for (Tree leaf : leaves)
  //     {
  //       for(int i = 0; i < board.getNoOfHoles(); i++)
  //       {
  //         Board temp = new Board(this.board);
  //         new Kalah(temp).makeMove(new Move(side, i+1));
  //
  //         this.addChild(new Tree(temp, side.opposite(), depth+1));
  //       } // for
  //     }
  //   }
  // } // generateBottomLayer

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
