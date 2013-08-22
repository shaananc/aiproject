/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 *
 * @author SHAANAN
 */
public class NegaScout {

    int maxdepth = 4;

    int Evaluate(gameBoard gb, List<Move> m) {

        int[] numbers = gb.getNumbers();

        //System.out.println(gb);
        //System.out.println(numbers[0] +":"+numbers[1]+"\n");

        if (gb.turn != gameBoard.WHITE) {
            return numbers[0] - numbers[1];
        } else {
            return numbers[1] - numbers[0];
        }
    }

    // TODO - evaluate use of depth.
    public int negascout(gameBoard gb, List<Move> p, int alpha, int beta, int d) {
        int a, b, t, i;


        if (d == maxdepth) {
            return Evaluate(gb, p);
        }

        List<List <Move>> successors = gb.getMoves();
        a = alpha;
        b = beta;

        i = 1;
        for (List <Move> moveList : successors) {
            t = -negascout(gb.executeCompound(moveList), moveList, -b, -a,d+1);
            if ((t > a) && (t < beta) && (i > 1) && (d < maxdepth - 1)) {
                a = -negascout(gb.executeCompound(moveList), moveList, -beta, -t,d+1);
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
        
        String boardString = "5\n"
                + "- - - - -\n"
                + "- W W - -\n"
                + "- B - - -\n"
                + "- - - - -\n";
        
        gameBoard gb = new gameBoard(new ByteArrayInputStream((boardString.toString()).getBytes()));
        System.out.println(gb);
        List<List <Move>> moves = gb.getMoves();
        for (List<Move> m : moves) {
            gameBoard c = gb.deepCopy();
            System.out.println(c.executeCompound(m));
            System.out.println(ns.negascout(c, m, Integer.MIN_VALUE, Integer.MAX_VALUE,1));
        }

    }
}
