/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SHAANAN
 */
// TODO: Change to 2 bit system
// Add counter for total number of jump states reachable
// Getter function for jump states to generate on demand
// Add function to get all one-jumps using checkJumpsfromSpace()
// In getMoves() get all one-jumps and for all of those get number of childjumps
// (Base case is when there are no more one-jumps from a given board)
// Change bad constants and turnBool, and turn
// Change executeMove to take in a list of moves to make
// Add counter for moves
// 11 black, 10, white, 00, clear, 01 X
public class gameBoard {

    private static final boolean WHITE = true;
    private static final boolean BLACK = false;
    boolean turn = WHITE;
    int[] dirs;
    BitSet pieces;
    BitSet mask;
    int size;
    int n;

    public gameBoard(int n) {
        this.dirs = new int[]{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};

        pieces = new BitSet();
        mask = new BitSet();
        this.n = n;
        size = n * n;
    }

    public gameBoard executeMove(Move move) {

        int x = move.x;
        int y = move.y;

        gameBoard gb = (gameBoard) this.deepCopy();


        int num = y * n + x;


        /* If the space is already taken, the move is invalid */
        if (gb.mask.get(num)) {
            throw new IllegalArgumentException();
        }




        /* implementing simple placement */
        gb.mask.set(num);

        if (turn == WHITE) {
            gb.pieces.set(num);
        } else {
            gb.pieces.clear(num);
        }

        if (!move.jump) {


            gb.turn = !turn;

        } else {
            gb.mask.clear(move.jumpedSquare);
            gb.pieces.set(move.jumpedSquare);
        }

        /* TODO IMPLEMENT JUMP LOGIC */
        return gb;
    }

    /* TODO: Is it best to return an integer list? 
     * Is this the best algorithm to get moves? */
    /* O(size) */
    public List<Move> getPlaceMoves() {
        List<Move> moves = new ArrayList();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int num = y * n + x;
                if (!mask.get(num)) {
                    Move m = new Move();
                    m.x = x;
                    m.y = y;
                    moves.add(m);
                }
            }
        }
        return moves;
    }

    public List<Move> getJumpMoves() {
        List<Move> moves = new ArrayList();

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int num = y * n + x;

                /* If the space is populated by your color and is a jump */
                if (mask.get(num) && (pieces.get(num) == turn)) {

                    moves.addAll(getSpaceJumps(x, y));

                }

            }
        }
        jumpDFS(moves, this);

        return moves;
    }

    public void jumpDFS(List<Move> moves, gameBoard gb) {
        //System.out.println("jdfs");

        for (Move move : moves) {

            gameBoard gb_prime = gb.executeMove(move);
            //System.out.println(gb_prime);
            //System.out.println(gb);
            List<Move> jumps = gb_prime.getSpaceJumps(move.x, move.y);
            move.compJumps.addAll(jumps);
            jumpDFS(move.compJumps, gb_prime);
            //move.compJumps.addAll(r);
        }
        //System.out.println("end jdfs");

        /* Base case, return no jumps */
        /* If the move is a jump, call DFS on it */
        /* For all jumps, generate successor board */
        /* Check if board already exists in the cache */
        /* Check surrounding squares in the successor board *
         /* Add functions - getplacemoves() getjumpmoves() */

    }

    /* TODO Consider caching results to minimize lookups? */
    /* TODO Add check for illegal move */
    /* TODO write spec - returns direction index, or -1 on move */
    /* TODO: change function to check from a full space rather than an empty */
    /* Returns the direction of all squares that would be jumped by a move */
    private List<Move> getSpaceJumps(int x, int y) {
        int num = y * n + x;

        List<Move> moves = new ArrayList();

        for (int i = 0; i < 9; i++) {
            try {
                int candidateSpace = num + dirs[i] * 2;
                // past edge of line
                if (candidateSpace < 0
                        || ((x + dirs[i] < n) && (x + dirs[i] * 2 >= n))
                        || ((x + dirs[i] >= 0) && (x + dirs[i] * 2 < 0))) {
                    continue;
                }
                // If on left edge

                if ((x == 0)
                        && ((i == 1) || (i == 8) || (i == 5))) {
                    continue;
                } else if ((x == n - 1)
                        && ((i == 0) || (i == 6) || (i == 4))) {
                    continue;
                }


                if (mask.get(num + dirs[i]) && !mask.get(candidateSpace)) {
                    Move m = new Move(candidateSpace % n, candidateSpace / n, true);
                    m.jumpedSquare = num + dirs[i];
                    moves.add(m);
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }

        return moves;
    }

    /* O(size) printing of the board */
    @Override
    public String toString() {
        String str = "";
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {

                int num = y * n + x;

                if (mask.get(num)) {
                    if (pieces.get(num)) {
                        str = str + "W";
                    } else {
                        str = str + "B";
                    }
                } else if (pieces.get(num)) {
                    str = str + "X";
                } else {
                    str = str + ".";
                }
            }
            str = str + "\n";
        }
        return str;

    }

    public gameBoard deepCopy() {
        gameBoard gb = new gameBoard(this.n);
        gb.turn = turn;
        gb.mask = (BitSet) mask.clone();
        gb.pieces = (BitSet) pieces.clone();
        return gb;
    }

    public static void main(String[] args) {
        gameBoard gb = new gameBoard(4);
        gb = gb.executeMove(new Move(1, 1));
        gb = gb.executeMove(new Move(2, 1));
        gb = gb.executeMove(new Move(1, 2));
        gb = gb.executeMove(new Move(3, 3));
        gb = gb.executeMove(new Move(0, 2));
        //gb = gb.executeMove(new Move(2,1));
        System.out.println(gb);
        //List<Move> jumps = gb.getSpaceJumps(2, 1);
        List<Move> moves = gb.getJumpMoves();
        Queue<Move> toplay = new LinkedList();
        toplay.add(moves.get(0));
        while (!toplay.isEmpty()) {
            Move m = toplay.remove();
            gb = gb.executeMove(m);
            if (!m.compJumps.isEmpty()) {
                toplay.add(m.compJumps.get(0));
            }
            System.out.println(gb);
        }
        int a = 1;
    }
}
