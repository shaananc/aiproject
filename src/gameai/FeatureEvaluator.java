/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.util.List;

/**
 *
 * @author shaananc
 */
public class FeatureEvaluator implements Evaluator {

    public double Evaluate(GameBoard gb, List<InternalMove> m, int playerId) {

        double ret = 0;
        int[] numbers = gb.getNumbers();
        double fracMine = (numbers[0] - numbers[1]) / (gb.n * gb.n);


        if (gb.isOver() || gb.n < 5) {
            ret = numbers[0] > numbers[1] ? 1 : -1;
            return ret;
        } else {
        }

        //ret = fracMine;
        ret = weightedSafe(gb)-0.1*gb.countJumpMoves();
        // -1 for odd max depth, 1 for even max depth?? where is my bug?
        return ret;
    }

    private double weightedSafe(GameBoard gb) {
        double sum = 0;
        double safeWeight = 1.1;
        
        for (int i = 0; i < gb.n * gb.n; i++) {
            if (gb.isWhite(i)) {
                double weight = (isSafe(i,gb) ? safeWeight: 1);
                sum += 1*weight;
            }
            if (gb.isBlack(i)) {
                double weight = (isSafe(i,gb) ? safeWeight: 1);
                sum -= 1*weight;
            }
        }
        
        return sum;

    }

    private boolean isSafe(int loc, GameBoard gb) {


        boolean top = loc / gb.n == 0 ? true : false;
        boolean bottom = loc / gb.n == gb.n - 1 ? true : false;

        boolean left = loc % gb.n == 0 ? true : false;
        boolean right = loc % gb.n == gb.n - 1 ? true : false;


        if ((top || bottom) && (left || right)) {
            return true;
        }

        //if top or bottom
        if (left || right) {
            if (gb.isSameColor(loc, loc + gb.n) && gb.isSameColor(loc, loc - gb.n)) {
                return true;
            } else { return false;}
        }

        // if left or right
        if (top || bottom) {
            if (gb.isSameColor(loc, loc - 1) && gb.isSameColor(loc, loc + 1)) {
                return true;
            } else { return false;}
        }

        for (Integer i : gb.getAdjacent(loc)) {
            if (!gb.isSameColor(i, loc)) {
                return false;
            }
        }

        return true;

    }
}
