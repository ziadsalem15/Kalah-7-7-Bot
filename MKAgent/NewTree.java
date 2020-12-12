package MKAgent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
public class NewTree
{

  private final Board board;
  private Side side;
  public Map<Integer, NewTree> children;
  private int score = Integer.MIN_VALUE;
  private int heuristicScore;
  private static final int MAXIMUM_DEPTH = 8;

  public NewTree(Board board, Side side)
  {
     this.board = board;
     this.side = side;
     this.children = null;
     //this.heuristicScore = Heuristics.getHeuristicScore(this);
  }

  public NewTree getChild(int lastMove)
  {
     return this.children.get(lastMove);
  }

  public Map<Integer, NewTree> getChildren()
  {
     if (this.children == null)
     {
        this.children = this.generateChildren();
     }
     return this.children;
  }

  private NewTree generateChild(int validChild)
  {
     Board tempBoard = new Board(this.board);
     Move tempMove = new Move(this.side, validChild);
     Side currentSide = Kalah.makeMove(tempBoard, tempMove);
     return new NewTree(tempBoard, currentSide);
  }

  public Map<Integer, NewTree> generateChildren()
  {
     List validHoles = this.board.getValidHoles(this.side);
     HashMap map = new HashMap(validHoles.size());
     Iterator iterateOnValid = validHoles.iterator();
     while(iterateOnValid.hasNext())
     {
        int validChild = (Integer)iterateOnValid.next();
        map.put(validChild, this.generateChild(validChild));
     }
     return map;
  }

  public void generateChildrenLayers()
  {
    if (this.children == null)
       this.children = this.getChildren();
    else
    {
       Iterator childrenValues = this.children.values().iterator();
       while(childrenValues.hasNext())
       {
          NewTree temp = (NewTree)childrenValues.next();
          temp.generateChildrenLayers();
       }
    }
  } // generateChildren
  public Side getSide()
  {
    return side;
  } // getSide
  public Board getBoard()
  {
    return board;
  } // getBoard
}
