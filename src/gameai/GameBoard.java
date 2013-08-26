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
public class GameBoard {

    protected static final boolean WHITE = true;
    protected static final boolean BLACK = false;
    boolean turn = WHITE;
    private int[] dirs;
    BitSet pieces;
    BitSet mask;
    int n;

    public GameBoard(int n) {
        this.dirs = new int[]{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};

        pieces = new BitSet(n);
        mask = new BitSet(n);
        pieces.set(0, n, false);
        mask.set(0, n, false);
        this.n = n;
    }

    public GameBoard(InputStream boardStream) {
        Scanner s = new Scanner(boardStream);
        n = s.nextInt();
        dirs = new int[]{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};

        pieces = new BitSet(n);
        mask = new BitSet(n);


        int loc = 0;
        while (s.hasNext()) {
            String t = s.next();
            if (t.equals("-")) {
            } else if (t.equals("B")) {
                mask.set(loc);
            } else if (t.equals("W")) {
                mask.set(loc);
                pieces.set(loc);
            } else if (t.equals("X")) {
                pieces.set(loc);
            } else if (t.equals("EOF")) {
                return;
            } else {
                System.out.println("Error parsing boardString -- unidentified char:" + t);
                System.exit(0);
            }
            loc += 1;
        }
    }

    // 11 black, 10, white, 00, clear, 01 X
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

    public boolean isEnemyPiece(int loc) {
        return (mask.get(loc) && (pieces.get(loc) != turn));
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

    public GameBoard executeMove(Move move) {

        int x = move.x;
        int y = move.y;

        GameBoard gb = (GameBoard) this.deepCopy();


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
        List<Move> moves = new ArrayList<Move>();
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

    public List<List<Move>> getJumpMoves() {
        List<List<Move>> all_moves = new ArrayList<List<Move>>();

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int num = y * n + x;
                /* If the space is populated by your color and is a jump */
                if (isMyPiece(num)) {
                    for (Move move : getSpaceJumps(x, y)) {
                        List<Move> path = new ArrayList<Move>();
                        path.add(move);
                        all_moves.add(path);
                        enumerateJumpTree(move, executeMove(move), path, all_moves);
                    }
                }
            }
        }


        return all_moves;
    }

    public void enumerateJumpTree(Move m, GameBoard gb, List<Move> path, List<List<Move>> all_moves) {
        //System.out.println("Enumerating Tree From: " + "\n");
        //System.out.println(gb);

        List<Move> possibleMoves = gb.getSpaceJumps(m.x, m.y);
        if (possibleMoves.isEmpty()) {
            return;
        } else {
            for (Move m_prime : possibleMoves) {
                List<Move> path_prime = new ArrayList<Move>(path);
                path_prime.add(m_prime);
                all_moves.add(path_prime);
                GameBoard gb_prime = gb.executeMove(m_prime);
                //System.out.println("move in jump tree: " + m.x + ":" + m.y + "\n");
                //System.out.println(gb);
                enumerateJumpTree(m_prime, gb_prime, path_prime, all_moves);
            }
        }
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

    public GameBoard executeCompound(List<Move> moves) {
        GameBoard gb = this.deepCopy();
        boolean jump = true;
        if (moves.get(0).jumpedSquare == Move.PLACE) {
            jump = false;
        }
        for (int i = 0; i < moves.size(); i++) {
            gb = gb.executeMove(moves.get(i));
        }
        // Flip turns for jump
        if (jump) {
            gb.turn = !gb.turn;
        }

        return gb;
    }

    public int jumpDFS(Move move, GameBoard gb) {

        int count = 0;

        GameBoard gb_prime = gb.executeMove(move);
        for (Move move_prime : gb_prime.getSpaceJumps(move.x, move.y)) {
            count += 1;
            count += jumpDFS(move_prime, gb_prime);
        }

        return count;
    }

    private List<Move> getSpaceJumps(int x, int y) {


        int num = y * n + x;

        // Right 0, Left 1, Down 2, Up 3, Down-Right 4, Down-Left 5, Up-Right 6, Up-Left, 7
        //{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};
        List<Move> moves = new ArrayList<Move>();

        for (int i = 0; i < 8; i++) {

            int candidateSpace = num + dirs[i] * 2;
            int jumpedSquare = num + dirs[i];

            // Check square on board
            if (candidateSpace > n * n - 1 || jumpedSquare > n * n - 1
                    || candidateSpace < 0 || jumpedSquare < 0) {
                continue;
            }

            // Left Out of Bounds
            if ((i == 5 || i == 7 || i == 1) && x - 2 < 0) {
                continue;
            }

            // Right Out of Bounds
            if ((i == 4 || i == 6 || i == 0) && x + 2 >= n) {
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

        }

        return moves;
    }

    /* O(size) printing of the board */
    @Override
    public String toString() {
        String str = "";
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {

                int loc = y * n + x;
                if (isWhite(loc)) {
                    str = str + "W";
                } else if (isBlack(loc)) {
                    str = str + "B";
                } else if (isDead(loc)) {
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

    public GameBoard deepCopy() {
        GameBoard gb = new GameBoard(this.n);
        gb.turn = turn;
        gb.mask = (BitSet) mask.clone();
        gb.pieces = (BitSet) pieces.clone();
        return gb;
    }

    public void printMoves(Collection<Move> moves) {
        Queue<Move> toplay = new LinkedList<Move>();
        toplay.addAll(moves);
        while (!toplay.isEmpty()) {
            System.out.println(this.executeMove(toplay.remove()));
        }
    }

    public int getDepth() {
        BitSet over = ((BitSet) pieces.clone());
        over.or(mask);
        return over.cardinality();
    }

    public boolean isOver() {

        return getDepth() >= n * n;
    }

    public int[] getNumbers() {
        int[] ret = new int[2];
        ret[0] = 0;
        ret[1] = 0;
        for (int i = 0; i < n * n; i++) {
            if (isWhite(i)) {
                ret[0] += 1;
            }
            if (isBlack(i)) {
                ret[1] += 1;
            }
        }
        return ret;
    }

    public List<List<Move>> getMoves() {
        List<List<Move>> ret = getJumpMoves();
        for (Move m : getPlaceMoves()) {
            List t = new ArrayList();
            t.add(m);
            ret.add(t);
        }
        return ret;

    }

    public static void run(InputStream in) {
        GameBoard gb = new GameBoard(in);
        gb.turn = GameBoard.WHITE;
        System.out.println("W " + gb.getPlaceMoves().size() + " " + gb.countJumpMoves());
        gb.turn = GameBoard.BLACK;
        System.out.println("B " + gb.getPlaceMoves().size() + " " + gb.countJumpMoves());
    }

    public static void main(String[] args) {
        run(System.in);
    }
}
