/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.PrintStream;
import java.util.List;

/**
 *
 * @author shaananc
 */
public class NegaPlayer implements Player, Piece {

    GameBoard state;
    int playerId;

    @Override
    public int getWinner() {
        if (!state.isOver()) {
            return 0;
        }
        int[] n = state.getNumbers();
        System.out.println("White: " + n[0] + " Black: " + n[1]);
        if (n[0] > n[1]) {
            return 1;
        } else if (n[1] > n[0]) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public int init(int n, int p) {
        state = new GameBoard(n);
        //state.turn = GameBoard.WHITE;
        playerId = p;

        return 1;
    }

    @Override
    public Move makeMove() {

        Move refMove = new Move();

        int rows[];
        int cols[];

        if (state.depth == 0) {
            refMove.IsPlaceMove = true;
            refMove.RowPositions = new int[1];
            refMove.ColPositions = new int[1];
            refMove.RowPositions[0] = 0;
            refMove.ColPositions[0] = 0;

            refMove.P = playerId;
            state = state.executeMove(refMove);
            return refMove;
        }

        List<InternalMove> moves = new NegaScout(playerId).chooseMove(state);



        int i = 0;

        if (moves.size() == 1 && moves.get(0).jumpedSquare == -2) {
            refMove.IsPlaceMove = true;
            rows = new int[1];
            cols = new int[1];
        } else {
            rows = new int[moves.size() + 1];
            cols = new int[moves.size() + 1];

            refMove.IsPlaceMove = false;
            // calculate first square
            InternalMove m0 = moves.get(0);
            int signy = (m0.jumpedSquare / state.n < m0.y) ? -1 : 1;
            int signx = (m0.jumpedSquare % state.n < m0.x) ? -1 : 1;
            int origx = m0.x + signx * 2 * Math.abs((m0.jumpedSquare % state.n) - m0.x);
            int origy = m0.y + signy * 2 * Math.abs((m0.jumpedSquare / state.n) - m0.y);
            rows[i] = origx;
            cols[i] = origy;
            i++;
        }

        for (InternalMove m : moves) {
            rows[i] = m.x;
            cols[i] = m.y;
            i++;
        }
        refMove.RowPositions = rows;
        refMove.ColPositions = cols;
        refMove.P = playerId;

        state = state.executeCompound(moves);


        return refMove;
    }

    @Override
    public void printBoard(PrintStream output) {
        System.out.println(state.toString());
    }

    @Override
    public int opponentMove(Move m) {

        state = state.executeMove(m);

        return 1;
    }
}
