package MKAgent;
import java.util.HashMap;

import java.io.FileWriter;
import java.io.IOException;


public class Tree
{
  private final HashMap<Integer,Tree> children;

  private final Board board;
  private Side side;

  public Tree(Board board, Side side)
  {
    // TODO: Set the heuristic score
    children = new HashMap<Integer,Tree>();

    // You shouldn't be able to change those values.
    this.board = board;
    this.side = side;
  }

  public void addChild(int index, Tree child)
  {
    children.put(index, child);
  } // addChild

  public void generateChildrenLayers(int noOfLayers)
  {
    int noOfChildren = board.getNoOfHoles();

    if(noOfLayers == 1)
      for(int i = 1; i < noOfChildren+1; i++)
      {
        Board state = new Board(board);
        Move nextMove = new Move(side, i);

        if(!Kalah.isLegalMove(state, nextMove))
          continue;

        Side tmpSide = Kalah.makeMove(state, nextMove);
        this.addChild(i, new Tree(state, tmpSide));
      } // for

    else if(noOfLayers > 1)
      for(int i = 1; i < noOfChildren+1; i++)
      {
        Board state = new Board(board);
        Move nextMove = new Move(side, i);

        if(!Kalah.isLegalMove(state, nextMove))
          continue;

        Side tmpSide = Kalah.makeMove(state, nextMove);

        Tree child = new Tree(state, tmpSide);
        this.addChild(i, child);
        child.generateChildrenLayers(noOfLayers-1);
      } // for
  } // generateChildren

  public HashMap<Integer,Tree> getChildren()
  {
    return new HashMap<Integer,Tree>(children);
  } // getChildren

  public Tree getChild(int holeNumber)
  {
    return children.get(holeNumber);
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
