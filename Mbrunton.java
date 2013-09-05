import java.io.PrintStream;

/* Implementation of Player interface */

import csproj.jumper.*;

public class Mbrunton implements Player, Piece {
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
        
        // TODO: fix bug(s) in MinimaxMoveFinder
        //moveFinder = new MinimaxMoveFinder(gb.n, player);
        moveFinder = new StupidMoveFinder(gb.n, player);
        return SUCCESS;
    }

    /* Decide on a move to make, apply it to Gameboard, and
     * return Move object
     */
    public Move makeMove() {
        return moveFinder.getMove(gb);
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
}

