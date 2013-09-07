/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author SHAANAN
 */
public class NegaScout implements Player, Piece {

    int maxdepth = 6;
    GameBoard state;
    int playerId;

    public NegaScout() {
    }

    int Evaluate(GameBoard gb, List<InternalMove> m) {

        //System.out.println("evaluating:");
        //System.out.println(gb);

        int[] numbers = gb.getNumbers();
        int ret = numbers[0] - numbers[1];


        return ret;
    }

    // TODO - evaluate use of depth.
    public ScoutRet negascout(GameBoard gb, List<InternalMove> p, int alpha, int beta, int d) {
        int b, i;
        ScoutRet t = new ScoutRet();
        ScoutRet a = new ScoutRet();
        // a is best score
        // t is current score

        // alpha: lower bound of expected value of tree
        // beta: upper bound of expected value of tree


        if (d == maxdepth || gb.isOver()) {
            return new ScoutRet(Evaluate(gb, p), p);
        }

        List<List<InternalMove>> successors = gb.getMoves();



        a.score = alpha;
        b = beta;

        i = 1;
        for (List<InternalMove> moveList : successors) {
            t = negascout(gb.executeCompound(moveList), moveList, -b, -a.score, d + 1);
            t.score = -t.score;
            t.moveList = moveList;

            if ((t.score > a.score) && (t.score < beta) && (i > 1) && (d < maxdepth - 1)) {
                a = negascout(gb.executeCompound(moveList), moveList, -beta, -t.score, d + 1);
                a.score = -a.score;
            }


            // if current score is better than best
            if (t.score > a.score) {
                if (d == -1) {
                    System.out.println("New Best Move! " + t.score + " from " + a.score + " at " + moveList.get(0).x + "," + moveList.get(0).y + ", depth: " + d);
                    System.out.println("******\n" + gb.executeCompound(moveList) + "*****");
                    int debug = 1;
                }

                a = t;


            }

            // outside bounds, prune and exit
            if (a.score >= beta) {
                return a;
            }
            b = a.score + 1;
        }


        return a;

    }

    public List<InternalMove> chooseMove(GameBoard gb) {

        ScoutRet t = negascout(gb, null, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        if (!t.moveList.isEmpty()) {
            //System.out.println("Output from Negamax: " + t.score + " for square " + t.moveList.get(0).x + ":" + t.moveList.get(0).y);
        } else {
            System.out.println("ERROR!");
            System.exit(0);
        }
        return t.moveList;

    }

    public static void main(String[] args) {
//        String boardString = "5\n"
//                + "- - - - -\n"
//                + "- W W - -\n"
//                + "- B - - -\n"
//                + "- - - - -\n";
        playHuman();
    }

    public static void playHuman() {
        NegaScout ns = new NegaScout();
        GameBoard gb = new GameBoard(5);
        System.out.println(gb);

        Scanner s = new Scanner(System.in);
        while (!gb.isOver()) {
            int x;
            int y;

            do {
                x = s.nextInt();
                y = s.nextInt();
            } while (gb.isFull(y * gb.n + x));
            System.out.println();


            InternalMove m = new InternalMove(x, y);
            gb = gb.executeMove(m);
            System.out.println(gb);
            if (gb.isOver()) {
                break;
            }

            List<InternalMove> e = ns.chooseMove(gb);
            gb = gb.executeCompound(e);
            System.out.println(gb);
        }



    }

    @Override
    public int getWinner() {
        if (!state.isOver()) {
            return 0;
        }
        int[] n = state.getNumbers();
        if (n[0] > n[1]) {
            return 1;
        } else if (n[1] < n[0]) {
            return 0;
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
        List<InternalMove> moves = chooseMove(state);

        Move refMove = new Move();

        int rows[];
        int cols[];
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
        System.out.println(this.playerId + "'s board");
        printBoard(System.out);

        return refMove;
    }

    @Override
    public void printBoard(PrintStream output) {
        System.out.println(state.toString());
    }

    @Override
    public int opponentMove(Move m) {

//        System.out.println(this.playerId + "'s board before");
//        printBoard(System.out);
//
//        if (m.IsPlaceMove != true) {
//            int a = 1;
//        }

        state = state.executeMove(m);
        //System.out.println(this.playerId + "'s board");
        //printBoard(System.out);
        return 1;
    }
}
