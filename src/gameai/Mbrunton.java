package gameai;

import java.io.PrintStream;

/* Implementation of Player interface */
//import csproj.jumper.*
public class Mbrunton implements Player, Piece {

    GameBoardMitch gb;
    int player;
    boolean illegalOpponentMoveFlag;
    MoveFinder moveFinder;
    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;

    /* initialise empty nxn GameBoardMitch, set player colour and illegal
     * move flag, initialise moveFinder object for selecting moves
     */
    public int init(int n, int p) {
        if (n <= 0 || (p != WHITE && p != BLACK)) {
            return FAILURE;
        }

        gb = new GameBoardMitch(n);
        player = p;
        illegalOpponentMoveFlag = false;
        moveFinder = new MinimaxAlphaBetaMoveFinder(n, p);

        return SUCCESS;
    }

    /* just for debugging purposes */
    public int init(int n, int p, String boardString) {
        if (n <= 0 || (p != WHITE && p != BLACK)) {
            return FAILURE;
        }

        gb = new GameBoardMitch(n, boardString);
        player = p;
        illegalOpponentMoveFlag = false;
        moveFinder = new MinimaxAlphaBetaMoveFinder(gb.n, player);
        return SUCCESS;
    }

    /* for comparing different MoveFinders */
    public int init(int n, int p, MoveFinder moveFinder) {
        if (n <= 0 || (p != WHITE && p != BLACK)) {
            return FAILURE;
        }

        gb = new GameBoardMitch(n);
        player = p;
        illegalOpponentMoveFlag = false;
        this.moveFinder = moveFinder;
        return SUCCESS;
    }

    /* Decide on a move to make, apply it to GameBoardMitch, and
     * return Move object
     */
    public Move makeMove() {
        Move m = moveFinder.getMove(gb);

        // DEBUGGING
        if (!gb.isLegalMove(m)) {
            System.out.println("Warning, about to self-apply illegal move:");
        }

        gb.applyMove(m);
        int cols[];
        cols = m.RowPositions;
        m.RowPositions = m.ColPositions;
        m.ColPositions = cols;

        return m;
    }

    /* check if opponent's move is legal, and if so apply it to GameBoardMitch,
     * otherwise, set "illegal move" flag
     */
    public int opponentMove(Move m) {
        int cols[];
        cols = m.RowPositions;
        m.RowPositions = m.ColPositions;
        m.ColPositions = cols;


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

    /* print string representation of GameBoardMitch to desired
     * output stream
     */
    public void printBoard(PrintStream output) {
        output.println(gb.toString());
    }
}
