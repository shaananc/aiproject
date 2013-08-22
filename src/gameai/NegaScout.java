/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.util.List;

/**
 *
 * @author SHAANAN
 */
public class NegaScout {

    int maxdepth = 3;

    int Evaluate(gameBoard gb, Move m) {

        int[] numbers = gb.getNumbers();

        //System.out.println(gb);

        if (gb.turn == gameBoard.WHITE) {
            return numbers[0] - numbers[1];
        } else {
            return numbers[1] - numbers[0];
        }
    }

    public int negascout(gameBoard gb, Move p, int alpha, int beta) {
        int a, b, t, i;

        int d = gb.getDepth();

        if (d == maxdepth) {
            return Evaluate(gb, p);
        }

        List<Move> successors = gb.getMoves();
        a = alpha;
        b = beta;

        i = 1;
        for (Move m : successors) {
            t = -negascout(gb.executeMove(m), m, -b, -a);
            if ((t > a) && (t < beta) && (i > 1) && (d < maxdepth - 1)) {
                a = -negascout(gb.executeMove(m), m, -beta, -t);
            }
            a = Math.max(a, t);
            if (a >= beta) {
                return a;
            }
            b = a + 1;
        }
        return a;

    }

    public static void main(String[] args) {
        NegaScout ns = new NegaScout();
        gameBoard gb = new gameBoard(System.in);
        System.out.println(gb);
        List<Move> moves = gb.getMoves();
        for (Move m : moves) {
            gameBoard c = gb.deepCopy();
            System.out.println(c.executeMove(m));
            System.out.println(ns.negascout(c, m, Integer.MIN_VALUE, Integer.MAX_VALUE));
        }

    }
}
