package MKAgent;

public class Agent
{
  public final int DEPTH = 10;
  public final int DECISION_TIME;
  public Board board;
  protected Kalah kalah;

  public void runMinMax(Side currentSide) throws Exception
  {
    // Board newboard = this.board.clone();
    // new BestMove(0, 0.0D);
    long startTime = System.currentTimeMillis();
    long timeLimit = 20000L;
    Minimax nextMove = new Minimax(currentSide, startTime, timeLimit);
    BestMove optimalMove = nextMove.runMove();

    if(kalah.isLegalMove(optimalMove) && !kalah.gameOver())
      kalah.makeMove(this.board, new Move(currentSide, optimalMove.hole));
    else
      throw new IllegalArgumentException("You are performing an illegal move!");

    Main.sendMsg(Protocol.createMoveMsg(optimalMove.hole));
  }

}
