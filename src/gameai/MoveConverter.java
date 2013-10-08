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
public class MoveConverter {

    public static Move InternaltoExternal(List<InternalMove> moves, int n, int playerId) {

        if(moves == null){
            int debug = 1;
        }
        
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
            int signy = (m0.jumpedSquare / n < m0.y) ? -1 : 1;
            int signx = (m0.jumpedSquare % n < m0.x) ? -1 : 1;
            int origx = m0.x + signx * 2 * Math.abs((m0.jumpedSquare % n) - m0.x);
            int origy = m0.y + signy * 2 * Math.abs((m0.jumpedSquare / n) - m0.y);
            rows[i] = origx;
            cols[i] = origy;
            i++;
        }

        for (InternalMove m : moves) {
            rows[i] = m.x;
            cols[i] = m.y;
            i++;
        }
        refMove.ColPositions = rows;
        refMove.RowPositions = cols;
        refMove.P = playerId;
    
        return refMove ;
    }
    
}
