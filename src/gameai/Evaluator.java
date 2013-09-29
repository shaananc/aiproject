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
public interface Evaluator {
    
    double Evaluate(GameBoard gb, List<InternalMove> m, int playerId);
    
}
