/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import static gameai.Piece.BLACK;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author SHAANAN
 */
public class NegaScout {

    int maxdepth = 6;
    int playerId;
    Evaluator evaluator;

    public NegaScout(int playerId, int maxdepth) {
        this.playerId = playerId;
        evaluator = new FeatureEvaluator();
        this.maxdepth = maxdepth;
    }

    public NegaScout(int playerId,int maxdepth,Evaluator e) {
        this(playerId, maxdepth);
        evaluator = e;
    }

    // TODO - evaluate use of depth.
    public ScoutRet negascout(GameBoard gb, List<InternalMove> p, double alpha, double beta, int d, int color) {
        double b, i;
        ScoutRet t;
        ScoutRet a = new ScoutRet();
        // a is best score
        // t is current score

        // alpha: lower bound of expected value of tree
        // beta: upper bound of expected value of tree


        if (d == maxdepth || gb.isOver()) {
            //System.out.println(gb + "Evaluated to: " + Evaluate(gb, p));
            return new ScoutRet(color*evaluator.Evaluate(gb, p, playerId), p);
        }
        //System.out.println("new negamax:\n" + gb);
        List<List<InternalMove>> successors = gb.getMoves();



        a.score = alpha;
        b = beta;

        i = 1;
        for (List<InternalMove> moveList : successors) {
            t = negascout(gb.executeCompound(moveList), moveList, -b, -a.score, d + 1, -color);
            t.score = -t.score;
            t.moveList = moveList;

            if ((t.score > a.score) && (t.score < beta) && (i > 1) && (d < maxdepth - 1)) {
                //System.out.println("Supplementary Search");
                a = negascout(gb.executeCompound(moveList), moveList, -beta, -t.score, d + 1, -color);
                a.score = -a.score;
                a.moveList = moveList;
            }




            // if current score is better than best
            if (t.score > a.score) {


                a = t;


            }

            // outside bounds, prune and exit
            if (a.score >= beta) {
                return a;
            }
            b = a.score + 1;
            i++;
        }


        return a;

    }

    public List<InternalMove> chooseMove(GameBoard gb) {

        ScoutRet t = negascout(gb, null, Integer.MIN_VALUE + 1, Integer.MAX_VALUE, 0, (playerId == 1 ? 1 : -1) );
        if (!t.moveList.isEmpty()) {
            //System.out.println("Output from Negamax: " + t.score + " for square " + t.moveList.get(0).x + ":" + t.moveList.get(0).y);
        } else {
            System.out.println("ERROR!");
            System.exit(0);
        }
        return t.moveList;

    }

    public static void main(String[] args) {
        playHuman();
    }

    public static void playHuman() {
        NegaScout ns = new NegaScout(1,3);
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
}
