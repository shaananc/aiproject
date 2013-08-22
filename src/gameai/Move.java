/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;


/**
 *
 * @author SHAANAN
 */

// Todo - Change x,y to a single int
public class Move  {

    protected static final int SELF_JUMP = -1;
    protected static final int PLACE = -2;
    int x, y;
    int jumpedSquare;

    public Move() {
        this.jumpedSquare = PLACE;
    }

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
        this.jumpedSquare = PLACE;
    }

    public Move(int x, int y, int jumpedSquare) {
        this(x,y);
        this.jumpedSquare = jumpedSquare;
    }
    
    
}
