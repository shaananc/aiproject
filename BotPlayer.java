import java.io.PrintStream;

/* Implementation of Player interface */

//import csproj.jumper.*

public class BotPlayer implements Player, Piece {
    Gameboard gb;
    int player;
    boolean illegalOpponentMoveFlag;
    MoveFinder moveFinder;
    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;

    /* initialise empty nxn Gameboard, set player colour and illegal
     * move flag, initialise moveFinder object for selecting moves
     */
    public int init(int n, int p) {
        if (n <= 0 || (p != WHITE && p != BLACK)) {
            return FAILURE;
        }

        gb = new Gameboard(n);
        player = p;
        illegalOpponentMoveFlag = false;
        moveFinder = new MinimaxAlphaBetaMoveFinder(n, p);
        
        return SUCCESS;
    }

    /* Decide on a move to make, apply it to Gameboard, and
     * return Move object
     */
    public Move makeMove() {
    	Move m = moveFinder.getMove(gb);
    	gb.applyMove(m);
    	return m;
    }

    /* check if opponent's move is legal, and if so apply it to Gameboard,
     * otherwise, set "illegal move" flag
     */
    public int opponentMove(Move m) {

    	if (gb.isLegalMove(m)) {
    		gb.applyMove(m);
    		return SUCCESS;
    	} else {
    		illegalOpponentMoveFlag = true;
    		return FAILURE;
    	}
    }

    /* return state of gameplay - if opponent has made illegal move, return
     * "invalid" code, otherwise return code for a white/black victory, draw
     * or unfinished game
     */
    public int getWinner() {
    	if (illegalOpponentMoveFlag) {
    		return INVALID;
    	} else {
    		return gb.getWinner();
    	}
    }

    /* print string representation of Gameboard to desired
     * output stream
     */
    public void printBoard(PrintStream output) {
    	output.println(gb.toString());
    }
    
    
    /* functions to help with optimisation
     */
    public void setBoard(Gameboard gb) {
    	this.gb = gb;
    }
    
    public void setMoveFinder(MoveFinder moveFinder) {
    	this.moveFinder = moveFinder;
    }
    
    public void setEvaluator(BoardEvaluator evaluator) {
    	moveFinder.setEvaluator(evaluator);
    }
    
    public void setMaxDepth(int maxDepth) {
    	moveFinder.setMaxDepth(maxDepth);
    }
}

