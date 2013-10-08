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
public class BasicEvaluator  {

    public double Evaluate(GameBoardShaanan gb, List<InternalMove> m, int playerId) {

        double ret = 0;
        int[] numbers = gb.getNumbers();
        double fracMine = (numbers[0] - numbers[1]) / (gb.n * gb.n);


        if (gb.isOver()) {
            ret = numbers[0] > numbers[1] ? 1 : -1;
            return ret;
        }
        
        ret = fracMine;

        return ret;
    }
}
