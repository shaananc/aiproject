/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SHAANAN
 */
public class gameBoard implements Cloneable {

    private static final int EMPTY = -1;
    private static final int WHITE = 1;
    private static final int BLACK = 0;
    private int turn = WHITE;
    int[] dirs;
    BitSet pieces;
    BitSet mask;
    BitSet dead;
    int size;
    int n;

    public gameBoard(int n) {
        this.dirs = new int[]{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};

        pieces = new BitSet();
        mask = new BitSet();
        dead = new BitSet();
        this.n = n;
        size = n * n;
    }

    public gameBoard executeMove(int x, int y) {

        gameBoard gb = null;
        try {
            gb = (gameBoard) this.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(gameBoard.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        int num = y * n + x;


        /* If the space is already taken, the move is invalid */
        if (gb.mask.get(num) || gb.dead.get(num)) {
            throw new IllegalArgumentException();
        }

        gb.mask.set(num);

        /* implementing simple placement */
        boolean turnBool = false;
        if (turn == WHITE) {
            gb.pieces.set(num);
            turnBool = true;
        }


        List<Integer> jumps = checkMove(x, y);
        if (jumps != null) {
            for (Integer i : jumps) {
                if (gb.pieces.get(num + dirs[i]) != turnBool) {
                    gb.dead.set(num + dirs[i]);
                }
            }
            
            //TODO: Check for compound jumps here, by checking around

        }

        /* switch turns */
        gb.turn = 1 - turn;

        /* TODO IMPLEMENT JUMP LOGIC */
        return gb;
    }

    public int getSpace(int x, int y) {
        int num = y * n + x;

        /* if the piece is not set */
        if (mask.get(num)) {
            return EMPTY;
            /* if the piece is set and is WHITE */
        } else if (pieces.get(num)) {
            return WHITE;
            /* if the piece is set and is BLACK */
        } else {
            return BLACK;
        }
    }

    /* TODO: Is it best to return an integer list? 
     * Is this the best algorithm to get moves? */
    /* O(size) */
    public List<Move> getMoves() {
        List<Move> moves = new ArrayList();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int num = y * n + x;
                if (!mask.get(num) || !dead.get(num)) {
                    Move m = new Move();
                    m.x = x;
                    m.y = y;
                    m.jumps = checkMove(x, y);
                    moves.add(m);
                }
            }
        }
        return moves;
    }

    /* TODO Consider caching results to minimize lookups? */
    /* TODO Add check for illegal move */
    /* TODO write spec - returns direction index, or -1 on move */
    private List<Integer> checkMove(int x, int y) {
        int num = y * n + x;

        boolean turnBool = false;
        if (turn == WHITE) {
            turnBool = true;
        }

        List<Integer> jumps = null;

        for (int i = 0; i < 9; i++) {
            try {

                if (mask.get(num + dirs[i])
                        && mask.get(num + dirs[i] * 2)
                        && pieces.get(num + dirs[i] * 2) == turnBool) {
                    if (jumps == null) {
                        jumps = new ArrayList();
                    }
                    jumps.add(i);
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }


        return jumps;
    }

    /* O(size) printing of the board */
    @Override
    public String toString() {
        String str = "";
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int num = y * n + x;
                if (dead.get(num)) {
                    str = str + "X";
                } else if (mask.get(num)) {
                    if (pieces.get(num)) {
                        str = str + "W";
                    } else {
                        str = str + "B";
                    }
                } else {
                    str = str + ".";
                }
            }
            str = str + "\n";
        }
        return str;

    }

    public static void main(String[] args) {
        gameBoard gb = new gameBoard(5);
        gb = gb.executeMove(0, 0);
        gb = gb.executeMove(1, 0);
        
        
        gb = gb.executeMove(4, 0);
        gb = gb.executeMove(3, 0);
        
        gb = gb.executeMove(2, 1);
        System.out.println(gb);
        List<Move> moves = gb.getMoves();
        int a = 1;
    }
}
