/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

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

    protected static final boolean WHITE = true;
    protected static final boolean BLACK = false;
    boolean turn = WHITE;
    private int[] dirs;
    BitSet pieces;
    BitSet mask;
    int n;

    public gameBoard(int n) {
        this.dirs = new int[]{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};

        pieces = new BitSet();
        mask = new BitSet();
        this.n = n;
    }

    public gameBoard(InputStream boardStream) {
        Scanner s = new Scanner(boardStream);
        n = s.nextInt();
        dirs = new int[]{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};

        pieces = new BitSet();
        mask = new BitSet();


        int loc = 0;
        while (s.hasNext()) {
            String t = s.next();
            switch (t) {
                case "-":
                    break;
                case "B":
                    mask.set(loc);
                    break;
                case "W":
                    mask.set(loc);
                    pieces.set(loc);
                    break;
                case "X":
                    pieces.set(loc);
                    break;
                case "EOF":
                    return;
                default:
                    System.out.println("Error parsing boardString -- unidentified char:" + t);
                    System.exit(0);
            }
            loc += 1;
        }
    }

    public boolean isWhite(int loc) {
        return (!pieces.get(loc) && mask.get(loc));
    }

    public boolean isBlack(int loc) {
        return (pieces.get(loc) && mask.get(loc));
    }

    public boolean isEmpty(int loc) {
        return (!pieces.get(loc) && !mask.get(loc));
    }

    public boolean isDead(int loc) {
        return (pieces.get(loc) && !mask.get(loc));
    }

    public boolean isMyPiece(int loc) {
        return (mask.get(loc) && (pieces.get(loc) == turn));
    }

    public boolean isFull(int loc) {
        return mask.get(loc);
    }

    public void setWhite(int loc) {
        pieces.set(loc);
        mask.set(loc);
    }

    public void setBlack(int loc) {
        pieces.clear(loc);
        mask.set(loc);
    }

    public void setEmpty(int loc) {
        pieces.clear(loc);
        mask.clear(loc);
    }

    public void setDead(int loc) {
        pieces.set(loc);
        mask.clear(loc);
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

        if (turn == WHITE) {
            gb.setWhite(num);
        } else {
            gb.setBlack(num);
        }

        if (move.jumpedSquare == Move.PLACE) {

            gb.turn = !turn;

        } else if (move.jumpedSquare != -1) {

            gb.setDead(move.jumpedSquare);
        }

        return gb;
    }

    /* TODO: Is it best to return an integer list? 
     * Is this the best algorithm to get moves? */
    /* O(size) */
    public List<Move> getPlaceMoves() {
        List<Move> moves = new ArrayList<>();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int num = y * n + x;
                if (isEmpty(num)) {
                    Move m = new Move();
                    m.x = x;
                    m.y = y;
                    moves.add(m);
                }
            }
        }
        return moves;
    }

    public int countPlaceMoves() {
        int count = 0;
        for (int loc = 0; loc < n; loc += 1) {
            if (isEmpty(loc)) {
                count += 1;
            }
        }
        return count;
    }

    public List<Move> getJumpMoves() {
        List<Move> moves = new ArrayList<>();

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int num = y * n + x;

                /* If the space is populated by your color and is a jump */
                if (isMyPiece(num)) {

                    for (Move move : getSpaceJumps(x, y)) {
                        jumpDFS(move, this);
                        moves.add(move);
                    }
                }
            }
        }


        return moves;
    }

    public int countJumpMoves() {
        int count = 0;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (isMyPiece(y * n + x)) {
                    for (Move move : getSpaceJumps(x, y)) {
                        count += 1;
                        count += jumpDFS(move, this);
                    }
                }
            }
        }
        return count;
    }

    public int jumpDFS(Move move, gameBoard gb) {

        int count = 0;

        gameBoard gb_prime = gb.executeMove(move);
        //System.out.println("gb'\n" + gb_prime);
        for (Move move_prime : gb_prime.getSpaceJumps(move.x, move.y)) {
            move.compJumps.add(move_prime);
            count += 1;
            count += jumpDFS(move_prime, gb_prime);
        }

        return count;
    }

    private List<Move> getSpaceJumps(int x, int y) {
        int num = y * n + x;
        
        // Right 0, Left 1, Down 2, Up 3, Down-Right 4, Down-Left 5, Up-Right 6, Up-Left, 7
        //{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            try {

                int candidateSpace = num + dirs[i] * 2;
                int jumpedSquare = num + dirs[i];

                // Check square on board
                if (candidateSpace > n * n - 1 || jumpedSquare > n*n-1 
                        || candidateSpace < 0 || jumpedSquare < 0) {
                    continue;
                }

                // Left Out of Bounds
                if((i == 5 || i == 7) && x-2<0){
                    continue;
                }
                
                // Right Out of Bounds
                if((i == 4 || i == 6) && x+2>=n){
                    continue;
                }
                
                // past edge of line
                if (((x + dirs[i] < n) && (x + dirs[i] * 2 >= n))
                        || ((x + dirs[i] >= 0) && (x + dirs[i] * 2 < 0))) {
                    continue;
                }
                // If on left edge

                if ((x == 0)
                        && ((i == 1) || (i == 7) || (i == 5))) {
                    continue;
                } else if ((x == n - 1)
                        && ((i == 0) || (i == 6) || (i == 4))) {
                    continue;
                }


                if (isFull(jumpedSquare) && isEmpty(candidateSpace)) {
                    int movesJump;
                    if (!isMyPiece(jumpedSquare)) {
                        movesJump = jumpedSquare;
                    } else {
                        movesJump = Move.SELF_JUMP;
                    }

                    moves.add(new Move(candidateSpace % n, candidateSpace / n, movesJump));
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
                    str = str + "-";
                }
                str = str + " ";
            }
            str = str.trim();
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

    public void printMoves(Collection<Move> moves) {
        Queue<Move> toplay = new LinkedList<>();
        toplay.addAll(moves);
        while (!toplay.isEmpty()) {
            System.out.println(this.executeMove(toplay.remove()));
        }

    }

    public static void run(InputStream in) {

        gameBoard gb = new gameBoard(in);
        gb.turn = gameBoard.WHITE;
        System.out.println("W " + gb.getPlaceMoves().size() + " " + gb.countJumpMoves());
        gb.turn = gameBoard.BLACK;
        System.out.println("B " + gb.getPlaceMoves().size() + " " + gb.countJumpMoves());

    }

    public static void main(String[] args) {
        run(System.in);

    }
}
