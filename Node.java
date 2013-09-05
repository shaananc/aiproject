import java.util.ArrayList;
import csproj.jumper.*;

/* Node for game trees
 * Keeps track of game state, and sequence of moves which
 * permutes root state into node state
 */

public class Node implements Piece {
	Gameboard gb;
	ArrayList<Node> childNodes;
	Move lastMove;
	int utility;
	int whosTurn;
	
	// constructor for root node
	public Node(Gameboard gb, int whosTurn) {
		this.gb = gb;
		this.lastMove = null;
		this.utility = INVALID;
		this.whosTurn = whosTurn;
	}
	
	// constructor for non-root node
	public Node(Gameboard gb, Move lastMove) {
		this.gb = gb;
		this.lastMove = lastMove;
		this.utility = INVALID;
		
		if (lastMove.P == WHITE) {
			whosTurn = BLACK;
		} else {
			whosTurn = WHITE;
		}
	}
		
}
