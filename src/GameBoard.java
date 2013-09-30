/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shaananc
 */
public interface GameBoard {

    public boolean isWhite(int loc);

    public boolean isBlack(int loc);

    public boolean isEmpty(int loc);

    public boolean isDead(int loc);

    public boolean isSameColor(int loc1, int loc2);

    public boolean isFull(int loc);

}
