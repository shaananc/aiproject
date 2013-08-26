/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author SHAANAN
 */
public class NegaScout {

    int maxdepth = 3;

    int Evaluate(GameBoard gb, List<Move> m) {

        //System.out.println("evaluating:");
        //System.out.println(gb);

        int[] numbers = gb.getNumbers();
        int ret = numbers[0] - numbers[1];


        // sample function to test
//        int sum = 0;
//        for (int i = 0; i < Math.pow(gb.n, 2); i++) {
//            if (gb.isMyPiece(i)) {
//                sum = sum + i + 1;
//            } else if (gb.isEnemyPiece(i)) {
//                sum = sum - i - 1;
//            }
//        }
//        if (sum != -100) {
//            return sum;
//        }


//        if (gb.turn != GameBoard.WHITE) {
//            ret = -ret;
//        } else {
//            ret = ret;
//        }


        //System.out.println("Ret: " + ret + " " + Integer.toString(numbers[0]) + " " + Integer.toString(numbers[1]) + "," + gb.turn + "\n");


        return ret;
    }

    // TODO - evaluate use of depth.
    public ScoutRet negascout(GameBoard gb, List<Move> p, int alpha, int beta, int d) {
        int b, i;
        ScoutRet t = new ScoutRet();
        ScoutRet a = new ScoutRet();
        // a is best score
        // t is current score

        // alpha: lower bound of expected value of tree
        // beta: upper bound of expected value of tree


        if (d == maxdepth || gb.isOver()) {
            return new ScoutRet(Evaluate(gb, p),p);
        }

        List<List<Move>> successors = gb.getMoves();



        a.score = alpha;
        b = beta;

        i = 1;
        for (List<Move> moveList : successors) {
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

    public List<Move> chooseMove(GameBoard gb) {

        ScoutRet t = negascout(gb, null, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        if(!t.moveList.isEmpty()){
        System.out.println("Output from Negamax: " + t.score + " for square " + t.moveList.get(0).x + ":" + t.moveList.get(0).y);
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
        GameBoard gb = new GameBoard(4);
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


            Move m = new Move(x, y);
            gb = gb.executeMove(m);
            System.out.println(gb);
            if (gb.isOver()) {
                break;
            }

            List<Move> e = ns.chooseMove(gb);
            gb = gb.executeCompound(e);
            System.out.println(gb);
        }



    }
}
