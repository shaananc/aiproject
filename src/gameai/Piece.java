package gameai;
/*
 *   Piece:
 *      Define types of pieces that can appear on a board
 *      
 *   @author msalehi
 *   
 */

public interface Piece {
    public static final int WHITE = 1, 
                            BLACK = 2,
                            DEAD = 3,
                            EMPTY = 0,
    		                INVALID = -1;
}
