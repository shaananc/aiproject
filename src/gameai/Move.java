/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.util.PriorityQueue;

/**
 *
 * @author SHAANAN
 */
public class Move implements Comparable<Move> {

    protected static final int SELF_JUMP = -1;
    protected static final int PLACE = -2;
    int x, y;
    int jumpedSquare;
    int numSubJumps;
    PriorityQueue<Move> compJumps;

    public Move() {
        this.jumpedSquare = PLACE;
    }

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
        this.jumpedSquare = PLACE;
        numSubJumps = 0;
    }

    public Move(int x, int y, int jumpedSquare) {
        this(x,y);
        this.jumpedSquare = jumpedSquare;
        this.compJumps = new PriorityQueue();
    }

    @Override
    public int compareTo(Move o) {
        return ((Integer)((Move)o).numSubJumps).compareTo(numSubJumps);
    }
    
}
