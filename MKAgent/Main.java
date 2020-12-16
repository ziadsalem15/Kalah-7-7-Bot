package MKAgent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * The main application class. It also provides methods for communication
 * with the game engine.
 */
public class Main
{
    /**
     * Input from the game engine.
     */
    private static Reader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Sends a message to the game engine.
     * @param msg The message.
     */
    public static void sendMsg (String msg)
    {
    	System.out.print(msg);
    	System.out.flush();
    }

    /**
     * Receives a message from the game engine. Messages are terminated by
     * a '\n' character.
     * @return The message.
     * @throws IOException if there has been an I/O error.
     */
    public static String recvMsg() throws IOException
    {
    	StringBuilder message = new StringBuilder();
    	int newCharacter;

    	do
    	{
    		newCharacter = input.read();
    		if (newCharacter == -1)
    			throw new EOFException("Input ended unexpectedly.");
    		message.append((char)newCharacter);
    	} while((char)newCharacter != '\n');

		return message.toString();
    }

	/**
	 * The main method, invoked when the program is started.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args)
	{
		new Agent().playGame();

    // Board board = new Board(7,7);
    // Side side = Side.SOUTH;
    // Tree tree = new Tree(board, side, 0);
    //
    // System.out.println("Root no of children: " + tree.getChildren().size());
    // // tree.generateChildrenLayers(8);
    // //
    // // Tree child = tree;
    // // for(int i = 1; i < 9; i++)
    // // {
    // //   System.out.println("Layer " + i + ": " + child.getChildren().size());
    // //   child = child.getChild(1);
    // // }
    // // System.out.println("Hello");
    //
    // tree.generateBottomLayer1(0);
    //
    // System.out.println();
    // System.out.println("Generated 1 more layer\n");
    // System.out.println();
    //
    // Tree child1 = tree;
    // for(int i = 1; i < 2; i++)
    // {
    //   System.out.println("Layer " + i + ": " + child1.getChildren().size());
    //   child1 = child1.getChild(1);
    // }
	}
}
