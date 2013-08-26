/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author SHAANAN
 */
public class NegaScout {

    int maxdepth = 2;
    List<Move> curbest;

    int Evaluate(gameBoard gb, List<Move> m) {

        System.out.println("evaluating:");
        System.out.println(gb);

        int[] numbers = gb.getNumbers();
        int ret = numbers[1] - numbers[0];


        //return m.get(0).x;

        if (gb.turn != gameBoard.WHITE) {
            ret = ret * -1;
        } else {
            ret = ret;
        }


        System.out.println("Ret: " + ret + " " + Integer.toString(numbers[0]) + " " + Integer.toString(numbers[1]) + "\n");


        return ret;
    }

    // TODO - evaluate use of depth.
    public int negascout(gameBoard gb, List<Move> p, int alpha, int beta, int d) {
        int a, b, t, i;


        if (d == maxdepth) {
            return Evaluate(gb, p);
        }

        List<List<Move>> successors = gb.getMoves();



        a = alpha;
        b = beta;

        i = 1;
        for (List<Move> moveList : successors) {
            t = -negascout(gb.executeCompound(moveList), moveList, -b, -a, d + 1);
            if ((t > a) && (t < beta) && (i > 1) && (d < maxdepth - 1)) {
                a = -negascout(gb.executeCompound(moveList), moveList, -beta, -t, d + 1);
            }
            a = Math.max(a, t);
            if (a >= beta) {
                curbest = p;
                return a;
            }
            b = a + 1;
        }
        return a;

    }

    public List<Move> chooseMove(gameBoard gb) {

        System.out.println(negascout(gb, null, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));

        return curbest;

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
        gameBoard gb = new gameBoard(2);
        System.out.println(gb);

        Scanner s = new Scanner(System.in);
        while (!s.hasNext("EOF")) {
            int x = s.nextInt();
            int y = s.nextInt();
            Move m = new Move(x, y);
            gb = gb.executeMove(m);
            System.out.println(gb);
            List<Move> e = ns.chooseMove(gb);
            if (e == null) {
                continue;
            }
            gb = gb.executeCompound(e);
            System.out.println(gb);
        }



    }
}
